package by.korotkov.chatbot.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "value_currencies")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ValueCurrency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserChatBot userChatBot;

    @Enumerated(EnumType.STRING)
    @Column(name = "Symbol")
    private Symbol symbol;

    @Column(name = "value")
    private Double value;
}
