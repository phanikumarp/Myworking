package n42.api;

import java.util.Date;

public interface IDomainMetadata {
	Date getN42CreatedDate();

	void setN42CreatedDate(Date date);

	Date getN42LastUpdatedDate();

	void setN42LastUpdatedDate(Date date);

	String getN42Hash();

	void setN42Hash(String hash);

	Integer getN42Id();

	void setN42Id(Integer n42Id);

	Integer getN42Version();

	void setN42Version(Integer n42Version);

}
