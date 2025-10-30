# UserFlow
# Documentation Spring Framework (Core, Data JPA, MVC)

## Table des matières

1. [Spring Core - IoC & DI](#spring-core)
2. [Spring Data JPA - Persistance](#spring-data-jpa)
3. [Spring MVC - Couche Web](#spring-mvc)

---

## SPRING CORE - Inversion de Contrôle & Injection de Dépendances {#spring-core}

### 1. Qu'est-ce que Spring Core et à quoi sert-il dans une application Java ?

**Spring Core** est le module fondamental du framework Spring. Il fournit le conteneur IoC (Inversion of Control) qui gère la création, la configuration et le cycle de vie des objets (beans) de l'application.

**Il sert à :**
- Gérer les dépendances entre les composants
- Réduire le couplage entre les classes
- Faciliter les tests unitaires
- Centraliser la configuration de l'application

---

### 2. Que signifie le principe d'Inversion de Contrôle (IoC) ?

L'**IoC** est un principe de conception où le contrôle de la création et de la gestion des objets est transféré du code applicatif vers un conteneur externe (le conteneur Spring). Au lieu que votre code crée ses dépendances avec `new`, c'est le framework qui les fournit.

**Avant IoC :**
```java
public class UserService {
    private UserRepository repository = new UserRepository(); // Couplage fort
}
```

**Avec IoC :**
```java
public class UserService {
    private UserRepository repository; // Fourni par Spring
}
```

---

### 3. Quelle est la différence entre IoC et Injection de Dépendances (DI) ?

- **IoC** : Principe général où le framework contrôle le flux d'exécution
- **DI** : Implémentation concrète de l'IoC qui consiste à injecter les dépendances nécessaires dans un objet

**En résumé :** L'IoC est le concept, la DI est la technique.

---

### 4. Qu'est-ce qu'un bean dans Spring ?

Un **bean** est un objet géré par le conteneur Spring. Il est :
- Instancié par Spring
- Configuré par Spring
- Assemblé avec ses dépendances par Spring
- Géré tout au long de son cycle de vie par Spring

```java
@Component
public class UserService {
    // Ce sera un bean Spring
}
```

---

### 5. Quel est le rôle du conteneur IoC ?

Le conteneur IoC (ou ApplicationContext) :
- **Instancie** les beans selon leur configuration
- **Injecte** les dépendances entre beans
- **Gère** le cycle de vie des beans (création, initialisation, destruction)
- **Configure** les beans selon les métadonnées (XML, annotations, Java Config)
- **Résout** les références entre beans

---

### 6. Quelle est la différence entre ApplicationContext et BeanFactory ?

| **BeanFactory** | **ApplicationContext** |
|-----------------|------------------------|
| Interface de base | Étend BeanFactory |
| Chargement lazy (à la demande) | Chargement eager (au démarrage) |
| Fonctionnalités minimales | Fonctionnalités avancées |
| Pas d'internationalisation | Support i18n |
| Pas de gestion d'événements | Publication d'événements |

**ApplicationContext** est préféré car plus complet.

---

### 7. Quelles sont les trois approches de configuration dans Spring ?

#### 1. Configuration XML
```xml
<bean id="userService" class="com.example.UserService">
    <property name="userRepository" ref="userRepository"/>
</bean>
```

#### 2. Configuration par Annotations
```java
@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;
}
```

#### 3. Configuration Java (Java Config)
```java
@Configuration
public class AppConfig {
    @Bean
    public UserService userService() {
        return new UserService(userRepository());
    }
}
```

---

### 8. À quoi servent les annotations suivantes ?

#### @Configuration
Indique qu'une classe contient des définitions de beans (méthodes @Bean)
```java
@Configuration
public class AppConfig {
    // Définitions de beans
}
```

#### @ComponentScan
Indique à Spring où chercher les composants annotés (@Component, @Service, etc.)
```java
@Configuration
@ComponentScan(basePackages = "com.example")
public class AppConfig { }
```

#### @Bean
Déclare qu'une méthode produit un bean géré par Spring
```java
@Bean
public UserService userService() {
    return new UserService();
}
```

#### @Component, @Service, @Repository, @Controller
- **@Component** : Composant générique Spring
- **@Service** : Logique métier (couche service)
- **@Repository** : Accès aux données (couche DAO)
- **@Controller** : Contrôleur MVC

#### @Autowired, @Qualifier
- **@Autowired** : Injection automatique par type
- **@Qualifier** : Précise quel bean injecter quand il y a plusieurs candidats

```java
@Autowired
@Qualifier("postgresRepository")
private UserRepository repository;
```

---

### 9. Comment Spring détecte et crée automatiquement les composants ?

Spring utilise le **component scanning** :

1. @ComponentScan définit les packages à scanner
2. Spring parcourt le classpath
3. Il détecte les classes annotées (@Component, @Service, etc.)
4. Il crée une définition de bean pour chacune
5. Il instancie et configure ces beans au démarrage

---

### 10. Quelles sont les étapes du cycle de vie d'un bean ?

1. **Instanciation** : Spring crée l'instance du bean
2. **Population des propriétés** : Injection des dépendances
3. **BeanNameAware.setBeanName()** : Si implémenté
4. **BeanFactoryAware.setBeanFactory()** : Si implémenté
5. **ApplicationContextAware.setApplicationContext()** : Si implémenté
6. **@PostConstruct** ou InitializingBean.afterPropertiesSet() : Initialisation custom
7. **Bean prêt à l'utilisation**
8. **@PreDestroy** ou DisposableBean.destroy() : Nettoyage avant destruction

```java
@Component
public class MyBean {
    @PostConstruct
    public void init() {
        // Initialisation
    }
    
    @PreDestroy
    public void cleanup() {
        // Nettoyage
    }
}
```

---

### 11. Quelle est la différence entre les scopes de bean ?

- **singleton** (défaut) : Une seule instance par conteneur Spring
- **prototype** : Une nouvelle instance à chaque demande
- **request** : Une instance par requête HTTP (web)
- **session** : Une instance par session HTTP (web)
- **application** : Une instance par ServletContext (web)

```java
@Component
@Scope("prototype")
public class MyBean { }
```

---

### 12. Pourquoi la configuration manuelle (avant Spring Boot) est-elle importante à comprendre ?

- **Compréhension profonde** : Savoir ce que fait Spring Boot automatiquement
- **Débogage** : Identifier les problèmes de configuration
- **Personnalisation** : Adapter les configurations auto-générées
- **Legacy** : Maintenir d'anciennes applications
- **Contrôle total** : Avoir la maîtrise complète de l'application

---

## SPRING DATA JPA - Persistance & Transactions {#spring-data-jpa}

### 13. Qu'est-ce que Spring Data JPA et quel problème résout-il ?

**Spring Data JPA** simplifie l'accès aux données en :
- Réduisant le code boilerplate (plus besoin d'écrire les requêtes CRUD)
- Fournissant des repositories avec méthodes prédéfinies
- Générant automatiquement les implémentations
- Offrant des requêtes dérivées des noms de méthodes

**Sans Spring Data :**
```java
public class UserRepositoryImpl {
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u").getResultList();
    }
    // Beaucoup de code répétitif...
}
```

**Avec Spring Data :**
```java
public interface UserRepository extends JpaRepository<User, Long> {
    // Tout est généré automatiquement !
    User findByEmail(String email); // Requête dérivée du nom
}
```

---

### 14. Quelle est la différence entre JPA et Hibernate ?

- **JPA** (Java Persistence API) : Spécification standard Java pour l'ORM (interface/contrat)
- **Hibernate** : Implémentation concrète de JPA (la plus populaire)

**Analogie :** JPA est comme une interface, Hibernate est comme une classe qui l'implémente.

JPA définit le "quoi", Hibernate définit le "comment".

---

### 15. Qu'est-ce qu'une entité JPA ?

Une **entité** est une classe Java qui représente une table de base de données.

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(length = 100)
    private String name;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    // Constructeurs, getters, setters
}
```

---

### 16. À quoi sert le DataSource ?

Le **DataSource** configure la connexion à la base de données :
- URL de connexion
- Driver JDBC
- Credentials (username/password)
- Pool de connexions

```java
@Bean
public DataSource dataSource() {
    DriverManagerDataSource ds = new DriverManagerDataSource();
    ds.setDriverClassName("org.postgresql.Driver");
    ds.setUrl("jdbc:postgresql://localhost:5432/warehouse_db");
    ds.setUsername("admin");
    ds.setPassword("password");
    return ds;
}
```

---

### 17. Que fait l'EntityManager ?

L'**EntityManager** est l'interface principale JPA pour interagir avec le contexte de persistance :

- **persist(entity)** : Persister des entités (INSERT)
- **find(Class, id)** : Rechercher des entités par ID (SELECT)
- **merge(entity)** : Mettre à jour des entités (UPDATE)
- **remove(entity)** : Supprimer des entités (DELETE)
- **createQuery(jpql)** : Exécuter des requêtes JPQL

```java
@Repository
public class UserDao {
    @PersistenceContext
    private EntityManager em;
    
    public User findById(Long id) {
        return em.find(User.class, id);
    }
}
```

---

### 18. Quelle est la responsabilité du TransactionManager ?

Le **TransactionManager** gère les transactions :
- Démarre les transactions
- Commit les modifications si tout est OK
- Rollback en cas d'erreur
- Gère les niveaux d'isolation
- Gère la propagation des transactions

```java
@Bean
public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
}
```

---

### 19. À quoi sert l'annotation @EnableJpaRepositories ?

Cette annotation active la création automatique des repositories Spring Data.

```java
@Configuration
@EnableJpaRepositories(basePackages = "com.example.repository")
public class JpaConfig {
    // Configuration JPA
}
```

Spring scanne le package et crée automatiquement les implémentations des interfaces repository.

---

### 20. Qu'est-ce qu'un repository Spring Data ?

Un **repository** est une interface qui fournit des méthodes d'accès aux données sans avoir à écrire l'implémentation.

```java
public interface UserRepository extends JpaRepository<User, Long> {
    // Méthodes héritées : save, findById, findAll, delete, etc.
    
    // Requêtes personnalisées dérivées du nom
    User findByEmail(String email);
    List<User> findByNameContaining(String name);
    
    // Requêtes JPQL personnalisées
    @Query("SELECT u FROM User u WHERE u.active = true")
    List<User> findActiveUsers();
}
```

---

### 21. Quelles sont les méthodes génériques fournies par JpaRepository ?

**Méthodes de base :**
- `save(entity)` : Sauvegarder ou mettre à jour
- `saveAll(entities)` : Sauvegarder plusieurs entités
- `findById(id)` : Trouver par ID (retourne Optional)
- `findAll()` : Récupérer toutes les entités
- `findAllById(ids)` : Trouver plusieurs par IDs
- `count()` : Compter le nombre d'entités
- `existsById(id)` : Vérifier l'existence
- `deleteById(id)` : Supprimer par ID
- `delete(entity)` : Supprimer une entité
- `deleteAll()` : Tout supprimer

**Méthodes avancées :**
- `findAll(Sort)` : Avec tri
- `findAll(Pageable)` : Avec pagination
- `flush()` : Forcer la synchronisation avec la DB

---

### 22. Comment gérer les transactions avec Spring ?

#### 1. Annotation @Transactional
```java
@Service
public class UserService {
    
    @Transactional
    public void createUser(User user) {
        userRepository.save(user);
        // Si erreur ici, rollback automatique
        emailService.sendWelcomeEmail(user.getEmail());
    }
}
```

#### 2. Niveaux de propagation
```java
@Transactional(propagation = Propagation.REQUIRED) // Défaut
@Transactional(propagation = Propagation.REQUIRES_NEW) // Nouvelle transaction
@Transactional(propagation = Propagation.MANDATORY) // Transaction obligatoire
@Transactional(propagation = Propagation.SUPPORTS) // Optionnel
@Transactional(propagation = Propagation.NOT_SUPPORTED) // Pas de transaction
@Transactional(propagation = Propagation.NEVER) // Erreur si transaction
```

#### 3. Gestion du rollback
```java
@Transactional(
    rollbackFor = Exception.class,  // Rollback sur toutes exceptions
    noRollbackFor = CustomException.class // Sauf cette exception
)
public void businessOperation() {
    // Code métier
}
```

#### 4. Isolation
```java
@Transactional(isolation = Isolation.READ_COMMITTED)
@Transactional(isolation = Isolation.REPEATABLE_READ)
@Transactional(isolation = Isolation.SERIALIZABLE)
```

---

### 23. Pourquoi définir manuellement la connexion à la base de données avant Spring Boot ?

Pour comprendre :
- Comment Spring se connecte à la base de données
- La configuration du pool de connexions
- Le cycle de vie des connexions
- Le fonctionnement interne de JPA
- Les problèmes de configuration potentiels

Spring Boot automatise tout cela, mais il est important de savoir ce qui se passe en arrière-plan.

---

### 24. Que doit contenir une configuration de persistance complète ?

```java
@Configuration
@EnableJpaRepositories(basePackages = "com.example.repository")
@EnableTransactionManagement
public class JpaConfig {
    
    // 1. DataSource - Connexion à la DB
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/mydb");
        ds.setUsername("user");
        ds.setPassword("password");
        return ds;
    }
    
    // 2. EntityManagerFactory - Gestion des entités
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.example.entity");
        
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", "true");
        em.setJpaProperties(properties);
        
        return em;
    }
    
    // 3. TransactionManager - Gestion des transactions
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
}
```

---

### 25. Qu'est-ce que la validation de contrainte dans le modèle ?

Utilisation de **Bean Validation (JSR-303)** pour valider automatiquement les données.

```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "L'email ne peut pas être null")
    @Email(message = "Format d'email invalide")
    @Column(unique = true)
    private String email;
    
    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String name;
    
    @Min(value = 18, message = "L'âge minimum est 18 ans")
    @Max(value = 120, message = "L'âge maximum est 120 ans")
    private Integer age;
    
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Numéro de téléphone invalide")
    private String phone;
}
```

**Validation dans le contrôleur :**
```java
@PostMapping("/users")
public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
    // La validation est automatique grâce à @Valid
    return ResponseEntity.ok(userService.save(user));
}
```

---

### 26. Quelle est la différence entre suppression logique et physique ?

#### Suppression physique (Hard Delete)
Suppression réelle de la ligne dans la base de données.
```java
userRepository.deleteById(id); // DELETE FROM users WHERE id = ?
```

#### Suppression logique (Soft Delete)
Marquage de l'enregistrement comme supprimé sans le supprimer physiquement.

```java
@Entity
public class User {
    @Column(name = "deleted")
    private Boolean deleted = false;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}

@Service
public class UserService {
    public void softDelete(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found"));
        user.setDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}

// Repository avec requêtes personnalisées
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.deleted = false")
    List<User> findAllActive();
}
```

**Avantages du Soft Delete :**
- Récupération possible des données
- Audit et traçabilité
- Historique complet
- Respect des contraintes d'intégrité référentielle

---

## SPRING MVC - Contrôleurs & Couche Web {#spring-mvc}

### 27. Que signifie MVC et quel est son objectif ?

**MVC (Model-View-Controller)** est un pattern architectural qui sépare l'application en 3 couches :

- **Model** : Données et logique métier (Entités, Services, Repositories)
- **View** : Présentation (HTML, JSON, XML)
- **Controller** : Traite les requêtes HTTP et coordonne Model/View

**Objectif :**
- Séparation des responsabilités
- Code plus maintenable
- Testabilité améliorée
- Réutilisabilité des composants

```
Requête HTTP → Controller → Service → Repository → Database
                    ↓
                  View (Réponse)
```

---

### 28. Quel est le rôle du DispatcherServlet ?

Le **DispatcherServlet** est le contrôleur frontal (Front Controller) qui :

1. **Reçoit** toutes les requêtes HTTP entrantes
2. **Route** les requêtes vers le bon contrôleur
3. **Gère** le flux de traitement complet
4. **Délègue** aux composants appropriés (HandlerMapping, HandlerAdapter, ViewResolver)
5. **Retourne** la réponse au client

C'est le point d'entrée unique de l'application web Spring MVC.

---

### 29. Quelle est la différence entre Controller et RestController ?

#### @Controller
Utilisé pour les applications web traditionnelles qui retournent des vues (HTML).

```java
@Controller
public class UserController {
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users"; // Nom de la vue JSP/Thymeleaf
    }
}
```

#### @RestController
Utilisé pour les APIs REST qui retournent des données (JSON/XML).

```java
@RestController
@RequestMapping("/api")
public class UserRestController {
    @GetMapping("/users")
    public List<User> listUsers() {
        return userService.findAll(); // Converti automatiquement en JSON
    }
}
```

**En résumé :** `@RestController = @Controller + @ResponseBody`

---

### 30. Fonction des annotations de mapping

#### @RequestMapping
Mappe une URL à une méthode de contrôleur (toutes méthodes HTTP).

```java
@RequestMapping(value = "/users", method = RequestMethod.GET)
public List<User> getUsers() {
    return userService.findAll();
}
```

#### @GetMapping, @PostMapping, @PutMapping, @DeleteMapping
Raccourcis spécialisés pour chaque méthode HTTP.

```java
@GetMapping("/users")           // Récupérer
@PostMapping("/users")          // Créer
@PutMapping("/users/{id}")      // Mettre à jour
@DeleteMapping("/users/{id}")   // Supprimer
```

#### @Valid
Déclenche la validation Bean Validation sur l'objet.

```java
@PostMapping("/users")
public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
    // Si validation échoue, exception MethodArgumentNotValidException
    return ResponseEntity.ok(userService.save(user));
}
```

#### @RequestBody
Désérialise le corps de la requête HTTP en objet Java.

```java
@PostMapping("/users")
public User create(@RequestBody User user) {
    // JSON → User (automatique)
    return userService.save(user);
}
```

#### @PathVariable
Récupère une variable depuis l'URL.

```java
@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) {
    return userService.findById(id);
}

@GetMapping("/users/{id}/orders/{orderId}")
public Order getOrder(@PathVariable Long id, @PathVariable Long orderId) {
    return orderService.findByUserAndId(id, orderId);
}
```

#### Autres annotations utiles

**@RequestParam :** Paramètres de requête
```java
@GetMapping("/users")
public List<User> search(@RequestParam String name) {
    // /users?name=John
    return userService.findByName(name);
}
```

**@ResponseStatus :** Définir le code HTTP de retour
```java
@PostMapping("/users")
@ResponseStatus(HttpStatus.CREATED)
public User create(@RequestBody User user) {
    return userService.save(user);
}
```

---

### 31. Comment le DispatcherServlet traite-t-il une requête HTTP du début à la fin ?

**Flux complet de traitement :**

1. **Réception** : DispatcherServlet reçoit la requête HTTP
2. **HandlerMapping** : Trouve le contrôleur correspondant à l'URL
3. **HandlerAdapter** : Adapte et invoque la méthode du contrôleur
4. **Interceptors (pre)** : Exécution des intercepteurs avant traitement
5. **Exécution** : Le contrôleur traite la requête
6. **Interceptors (post)** : Exécution des intercepteurs après traitement
7. **ViewResolver** : Résout la vue (si nécessaire)
8. **Rendu** : La vue est rendue en HTML/JSON
9. **Réponse** : Envoi de la réponse HTTP au client

```
Client → DispatcherServlet → HandlerMapping → HandlerAdapter 
         → Controller → Service → Repository → Database
         → Controller → ViewResolver → View → Client
```

---

### 32. Qu'est-ce que la classe WebConfig et à quoi sert-elle ?

La classe **WebConfig** configure la couche web de Spring MVC.

```java
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.example.controller")
public class WebConfig implements WebMvcConfigurer {
    
    // Configuration du ViewResolver pour JSP
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/", ".jsp");
    }
    
    // Configuration des ressources statiques (CSS, JS, images)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/");
    }
    
    // Configuration CORS
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
    
    // Configuration des intercepteurs
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor())
                .addPathPatterns("/api/**");
    }
    
    // Configuration du MessageConverter pour JSON
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converters.add(converter);
    }
}
```

---

### 33. Pourquoi initialiser le DispatcherServlet manuellement avant Spring Boot ?

**Raisons pédagogiques :**
- Comprendre le cycle de vie d'une application web
- Connaître la configuration du ServletContext
- Maîtriser le chargement du contexte Spring
- Comprendre l'intégration avec le conteneur de servlets

**Avant Spring Boot, il fallait :**
1. Configurer le web.xml OU WebAppInitializer
2. Déclarer le DispatcherServlet
3. Lier le contexte Spring au servlet
4. Configurer les mappings d'URL

Spring Boot fait tout cela automatiquement, mais comprendre le processus manuel est essentiel pour le débogage et la personnalisation.

---

### 34. Qu'est-ce qu'un WebAppInitializer et pourquoi remplace-t-il web.xml ?

**WebAppInitializer** est une classe Java qui remplace le fichier XML `web.xml` pour la configuration de l'application web.

```java
public class WebAppInitializer 
    extends AbstractAnnotationConfigDispatcherServletInitializer {
    
    // Configuration du contexte racine (Services, Repositories)
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { AppConfig.class, JpaConfig.class };
    }
    
    // Configuration du contexte web (Controllers)
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebConfig.class };
    }
    
    // Mapping du DispatcherServlet
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" }; // Toutes les URLs
    }
    
    // Filtres
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        return new Filter[] { encodingFilter };
    }
}
```

**Avantages par rapport à web.xml :**
- Configuration en Java (type-safe)
- Refactoring plus facile
- Pas de duplication entre XML et Java
- Plus moderne et flexible

---

### 35. Quelles sont les étapes de traitement d'une requête REST dans Spring MVC ?

**Flux complet d'une requête REST :**

1. **Client** envoie requête HTTP (ex: POST /api/users avec JSON)
2. **DispatcherServlet** reçoit la requête
3. **HandlerMapping** trouve le @RestController correspondant
4. **HandlerAdapter** prépare l'invocation de la méthode
5. **HttpMessageConverter** désérialise JSON → Objet Java
6. **@Valid** valide l'objet (si présent)
7. **Méthode du contrôleur** s'exécute
8. **Service** traite la logique métier
9. **Repository** accède à la base de données
10. **Contrôleur** retourne l'objet Java
11. **HttpMessageConverter** sérialise Objet → JSON
12. **Réponse HTTP** envoyée au client avec le JSON

```java
// Exemple complet
@RestController
@RequestMapping("/api/users")
public class
