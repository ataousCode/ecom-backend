package com.almousleck.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class Image {
    @Id
    @SequenceGenerator(
            name = "image_id_seq",
            sequenceName = "image_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "image_id_seq"
    )
    private Long id;
    private String fileName;
    private String fileType;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private Blob image;
    private String downloadUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
