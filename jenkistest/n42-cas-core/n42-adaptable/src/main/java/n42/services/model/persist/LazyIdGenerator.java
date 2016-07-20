package n42.services.model.persist;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentityGenerator;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;

/**
 * <b>native-if-not-assigned</b><br>
 * <br>
 * By setting the Hibernate configuration's primary key column
 * to use a "native" implementation, Hibernate ALWAYS generates
 * the entity's id when it is being saved. There is no way to
 * "override" the generated id.
 * 
 * This IdentityGenerator allows a programmer to override the
 * "generated" id, with an "assigned" id at runtime by simply
 * setting the primary key property.
 * 
 * @author ipsg
 */
public class LazyIdGenerator extends IdentityGenerator implements Configurable {

	private String entityName;

	@Override
	public Serializable generate(SessionImplementor session, Object entity) {
		Serializable id;
		EntityPersister persister = session.getEntityPersister(entityName, entity);
		// Determine if an ID has been assigned.
		id = persister.getIdentifier(entity, session);
		if (id == null) {
			// If the id was NOT assigned, return the POST_INSERT_INDICATOR,
			// which will determine and use the natively generated identifier.
			id = org.hibernate.id.IdentifierGeneratorHelper.POST_INSERT_INDICATOR;
			//return super.generate(session, entity);
		}
		return id;
	}

	@Override
	public void configure(Type type, Properties params, Dialect dialect) {
		this.entityName = params.getProperty(ENTITY_NAME);
		if (entityName == null) {
			throw new MappingException("no entity name");
		}
	}

}