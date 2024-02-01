package apap.tk.rumahsehat.generator;

import java.io.Serializable;
import java.util.stream.Stream;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;


public class AppointmentGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(
      SharedSessionContractImplementor session, Object obj) 
      throws HibernateException {

        var query = String.format("select %s from %s",
            session.getEntityPersister(obj.getClass().getName(), obj)
              .getIdentifierPropertyName(),
            obj.getClass().getSimpleName());

        Stream<String> ids = session.createQuery(query).stream();
 
        Long max = ids.map(o -> o.replace("APT-", ""))
          .mapToLong(Long::parseLong)
          .max()
          .orElse(0L);

        return "APT-" + (max + 1);
    }

} 
