package by.korotkov.chatbot.repositories;

import by.korotkov.chatbot.models.Symbol;
import by.korotkov.chatbot.models.UserChatBot;
import by.korotkov.chatbot.models.ValueCurrency;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class CurrencyRepository {
    @Autowired
    private SessionFactory sessionFactory;

    public void addOrUpdateCurrency(Long userId, Symbol symbol, Double value){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        ValueCurrency existingValueCurrency = session
                .createQuery("FROM ValueCurrency WHERE userChatBot.userId = :userId AND symbol = :symbol", ValueCurrency.class)
                .setParameter("userId", userId)
                .setParameter("symbol", symbol)
                .uniqueResult();
        if (existingValueCurrency != null) {
            existingValueCurrency.setValue(value);
            session.merge(existingValueCurrency);
        } else {
            UserChatBot user = session.find(UserChatBot.class, userId);
            ValueCurrency newValueCurrency = new ValueCurrency();
            newValueCurrency.setUserChatBot(user);
            newValueCurrency.setSymbol(symbol);
            newValueCurrency.setValue(value);
            session.persist(newValueCurrency);
        }
        transaction.commit();
        session.close();
    }
}
