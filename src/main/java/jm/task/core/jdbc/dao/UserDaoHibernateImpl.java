package jm.task.core.jdbc.dao;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {
    }


    @Override
    public void createUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery("CREATE TABLE if not exists userstable (" +
                    "  id BIGINT NOT NULL AUTO_INCREMENT," +
                    "  name VARCHAR(45) NULL," +
                    "  lastName VARCHAR(45) NULL," +
                    "  age TINYINT NULL," +
                    "  PRIMARY KEY (id)," +
                    "  UNIQUE INDEX id_UNIQUE (id ASC) VISIBLE);").executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS userstable;").executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(session.get(User.class, id));
            transaction.commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<User> getAllUsers() {
        try (Session session = Util.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);
            criteriaQuery.select(root);
            Query<User> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("DELETE FROM User");
            query.executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }
}