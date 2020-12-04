package org.lordrose.vrms.domains.bases;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class TimeAuditable<T> implements Serializable {

    @CreatedDate
    @Column(name = "created_at")
    private T createAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private T updatedAt;
}
