package io.okami101.realworld.core.user;

import io.okami101.realworld.core.article.Article;
import io.okami101.realworld.core.article.Comment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String name;

  private String password;

  private String bio;

  private String image;

  @CreationTimestamp
  @Column(nullable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private Instant updatedAt;

  @ManyToMany
  @JoinTable(
      name = "follower_user",
      joinColumns = {@JoinColumn(name = "following_id")},
      inverseJoinColumns = {@JoinColumn(name = "follower_id")})
  @BatchSize(size = 20)
  private Set<User> followers = new HashSet<>();

  @ManyToMany(mappedBy = "favoritedBy")
  private Set<Article> favoriteArticles = new HashSet<>();

  @OneToMany(mappedBy = "author")
  private Set<Article> articles = new HashSet<>();

  @OneToMany(mappedBy = "author")
  private Set<Comment> comments = new HashSet<>();
}
