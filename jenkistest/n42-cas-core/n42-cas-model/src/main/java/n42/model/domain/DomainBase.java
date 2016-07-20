package n42.model.domain;

import java.util.Date;

/**
 * Any persistence model must extend this class.
 *
 * @author ipsg
 */
public abstract class DomainBase { 

	public abstract Date getN42CreatedDate();

	public abstract void setN42CreatedDate(Date date);

	public abstract Date getN42LastUpdatedDate();

	public abstract void setN42LastUpdatedDate(Date date);

	public abstract String getN42Hash();

	public abstract void setN42Hash(String hash);

	public abstract Integer getN42Id();

	public abstract void setN42Id(Integer n42Id);

    public abstract Boolean getActive();
    
    public abstract void setActive(Boolean active);
    

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
