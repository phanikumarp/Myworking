package n42.model.domain;

import java.beans.Transient;
import java.util.Date;

import n42.api.IDomainMetadata;
import n42.api.IModelTypeInfo;
import n42.services.serialize.IModelDefinition;

/**
 * Any persistence model must extend this class.
 *
 * @author ipsg
 */
public abstract class DomainBase implements IDomainMetadata, IModelTypeInfo { 

	private transient IModelDefinition modelDefinition;

	@Transient
	@Override
	public String getModelName() {
		return getClass().getName();
	}

	@Transient
	@Override
	public IModelDefinition getModelDefinition() {
		return modelDefinition;
	}


	@Override
	public abstract Date getN42CreatedDate();

	@Override
	public abstract void setN42CreatedDate(Date date);

	@Override
	public abstract Date getN42LastUpdatedDate();

	@Override
	public abstract void setN42LastUpdatedDate(Date date);

	@Override
	public abstract String getN42Hash();

	@Override
	public abstract void setN42Hash(String hash);

	@Override
	public abstract Integer getN42Id();

	@Override
	public abstract void setN42Id(Integer n42Id);

	@Override
	public boolean equals(Object obj) {
		
		if (this.getN42Id() == null){
			return super.equals(obj);
		}
		
		if (obj instanceof DomainBase){
			if (obj.getClass().equals(this.getClass())){
				if (this.getN42Id().equals(((DomainBase)obj).getN42Id())){
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		if (this.getN42Id() == null){
			return super.hashCode();
		}
		return this.getN42Id().hashCode();
	}


	@Override
	public String toString() {
		final String message;

		if (null == getN42Id()) {
			message = getClass().getSimpleName() + "@new";
		} else {
			message = getClass().getSimpleName() + "@" + getN42Id();
		}

		return message;
	}

}
