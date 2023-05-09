package com.example.springsecuritydailycode.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class VerificationToken {

    // token expire after 10 min
    private static final int EXPIRATION_TIME = 10;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expirationTime;

    @OneToOne(fetch =  FetchType.EAGER)
    @JoinColumn(
            name="user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN")

    )
    private User user;

    public VerificationToken(User user, String token)
    {
        super();
        this.user=user;
        this.token=token;
        this.expirationTime = calculateExpireDate(EXPIRATION_TIME);
    }

    public VerificationToken(String token) {
        this.token = token;
        this.expirationTime = calculateExpireDate(EXPIRATION_TIME);
    }
    private Date calculateExpireDate(int expirationtime)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,expirationtime);
        return new Date(calendar.getTime().getTime());
    }
}
