package n42.storage.serialize;

import java.util.Date;

import n42.model.domain.DomainBase;

public class DomainBaseFixture extends DomainBase {

	private Integer n42Id;
	private String n42Hash;
	private Integer n42Version;
	private Date n42CreatedDate;
	private Date n42LastUpdatedDate;

	@Override
	public Date getN42CreatedDate() {
		return this.n42CreatedDate;
	}

	@Override
	public String getN42Hash() {
		return this.n42Hash;
	}

	@Override
	public Integer getN42Id() {
		return n42Id;
	}

	@Override
	public Date getN42LastUpdatedDate() {
		return this.n42LastUpdatedDate;
	}

	@Override
	public Integer getN42Version() {
		return this.n42Version;
	}

	@Override
	public void setN42CreatedDate(Date date) {
		this.n42CreatedDate = date;
	}

	@Override
	public void setN42Hash(String hash) {
		this.n42Hash = hash;
	}

	@Override
	public void setN42Id(Integer n42Id) {
		this.n42Id = n42Id;
	}

	@Override
	public void setN42LastUpdatedDate(Date date) {
		this.n42LastUpdatedDate = date;
	}

	@Override
	public void setN42Version(Integer n42Version) {
		this.n42Version = n42Version;
	}
}
