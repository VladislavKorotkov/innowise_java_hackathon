package by.korotkov.chatbot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserChatBot {
    @Id
    @Column(name="user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "percent")
    private Percent percent;

    @OneToMany(mappedBy = "userChatBot", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<ValueCurrency> valueCurrencies;
}
