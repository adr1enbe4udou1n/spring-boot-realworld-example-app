package io.okami101.realworld.core.article;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.okami101.realworld.core.user.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.FetchType;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" })
@Entity
@Table(name = "articles")

public class Article {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @ManyToOne(fetch = FetchType.EAGER)
        private User author;

        @Column(nullable = false)
        private String title;

        @Column(nullable = false, unique = true)
        private String slug;

        @Column(nullable = false)
        private String description;

        @Column(nullable = false)
        private String body;

        @CreationTimestamp
        @Column(nullable = false)
        private Instant createdAt;

        @UpdateTimestamp
        @Column(nullable = false)
        private Instant updatedAt;

        @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
        private Set<Comment> comments = new HashSet<>();

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "article_tag", joinColumns = { @JoinColumn(name = "article_id") }, inverseJoinColumns = {
                        @JoinColumn(name = "tag_id") })
        private Set<Tag> tags = new HashSet<>();

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "article_favorite", joinColumns = { @JoinColumn(name = "article_id") }, inverseJoinColumns = {
                        @JoinColumn(name = "user_id") })
        private Set<User> favoritedBy = new HashSet<>();
}
