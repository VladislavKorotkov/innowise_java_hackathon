package by.korotkov.chatbot.repositories;

import by.korotkov.chatbot.models.UserChatBot;
import jakarta.transaction.Transactional;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class UserChatBotRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void saveOrUpdateUser(UserChatBot userChatBot) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(userChatBot);
    }

    public List<UserChatBot> getAllUsers() {
        Session session = sessionFactory.getCurrentSession();
        Query<UserChatBot> query = session.createQuery("FROM UserChatBot", UserChatBot.class);
        return query.getResultList();
    }
}