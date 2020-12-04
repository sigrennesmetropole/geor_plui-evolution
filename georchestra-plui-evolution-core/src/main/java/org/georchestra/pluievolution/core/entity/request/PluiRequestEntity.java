package org.georchestra.pluievolution.core.entity.request;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

import com.vividsolutions.jts.geom.Geometry;
import org.georchestra.pluievolution.core.common.LongId;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.georchestra.pluievolution.core.dto.PluiRequestStatus;
import org.georchestra.pluievolution.core.dto.PluiRequestType;
import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;

/**
 * @author FNI18300
 *
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "plui_request")
public class PluiRequestEntity implements LongId {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "uuid", nullable = false, columnDefinition = "uuid")
	private UUID uuid;

	@Column(name = "redmine_id")
	private String redmineId;

	@Column(name = "subject", length = 30, nullable = false)
	private String subject;

	@Column(name = "object", length = 300, nullable = false)
	private String object;

	@Column(name = "comment", length = 1024)
	private String comment;

	@Column(name = "initiator", length = 150)
	private String initiator;

	@Column(name = "creation_date", nullable = false)
	private Date creationDate;

	@Column(name = "geometry", columnDefinition = "geometry")
	private Geometry geometry;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 50)
	private PluiRequestStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", length = 20)
	private PluiRequestType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area_id")
	private GeographicAreaEntity area;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PluiRequestEntity)) return false;

		PluiRequestEntity that = (PluiRequestEntity) o;

		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
		if (!getUuid().equals(that.getUuid())) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getUuid().hashCode();
		return result;
	}
}
