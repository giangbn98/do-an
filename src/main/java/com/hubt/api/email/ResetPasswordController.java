package com.hubt.api.email;

import com.hubt.data.GbException;
import com.hubt.data.Response;
import com.hubt.data.model.PasswordResetToken;
import com.hubt.data.model.User;
import com.hubt.data.repository.PasswordResetTokenRepostitory;
import com.hubt.data.repository.UserRepository;
import com.hubt.service.ResetPasswordService;
import com.hubt.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.time.chrono.MinguoEra;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/resetPassword")
@RestController
public class ResetPasswordController {

    private final int expiryHour = 2;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepostitory passwordResetTokenRepostitory;

    @Autowired
    private ResetPasswordService resetPasswordService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${email.from.address}")
    private String fromAddress;

    @PostMapping
    public ResponseEntity resetPassword(@RequestParam String email, HttpServletRequest request){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GbException("Email không tồn tại"));

        String token = resetPasswordService.generateToken();
        Date expiryDate = DateUtils.addHour(new Date(),expiryHour);
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setExpiryDate(expiryDate);
        resetToken.setUser(user);
        passwordResetTokenRepostitory.save(resetToken);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(new InternetAddress(fromAddress,"Gb"));
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Thiết lập lại mật khẩu");
            mimeMessageHelper.setText("Xin chào "+user.getUsername()+" , \n"
                    +"Bạn đã yêu cầu thiết lập lại mật khẩu của mình \n"
                    +"Để thiết lập lại, bạn cần click vào đường link sau: \n"
                    +"Lưu ý: Link reset mật khẩu chỉ có hiện lực trong 2 giờ. \n"
                    +"http://localhost:8080/login?code="+token);
            javaMailSender.send(mimeMessage);
        }catch (Exception e){
            e.getMessage();
        }
        return Response.data(user);
    }

    @GetMapping("/confirmResetPassword")
    public ResponseEntity confirmResetPassword(@RequestParam String code){
        if (resetPasswordService.validTokenResetPassword(code)){
            return Response.data("success");
        }else {
            return Response.error(HttpStatus.NOT_FOUND, "Code đã quá thời hạn");
        }
    }

    @PostMapping("/saveNewPassword")
    public ResponseEntity saveNewPassword(@RequestParam String newPassword, @RequestParam String code){
        PasswordResetToken passwordResetToken =
                passwordResetTokenRepostitory.findByToken(code)
                        .orElseThrow(()-> new GbException("error"));
        User user =
                userRepository.findById(passwordResetToken.getUser().getId())
                        .orElseThrow(() ->new  GbException("user dose not exits"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepostitory.deleteById(passwordResetToken.getId());
        return Response.data(user);
    }
}
