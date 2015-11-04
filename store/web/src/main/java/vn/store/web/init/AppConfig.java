/**
 * 
 */
package vn.store.web.init;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Siva
 *
 */
@Configuration
@ComponentScan(basePackages= { "vn.store" })
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "vn.store.service.repositories" }, entityManagerFactoryRef="entityManagerFactory")
@PropertySource("classpath:application.properties")
public class AppConfig 
{
	
	@Autowired
	private Environment env;
	
	@Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer()
    {
        return new PropertySourcesPlaceholderConfigurer();
    }
	
	@Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource)
    {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(Boolean.TRUE);
        vendorAdapter.setShowSql(Boolean.TRUE);

        factory.setDataSource(dataSource);
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("vn.store.jpa.domain");

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        jpaProperties.put("hibernate.dialec", env.resolvePlaceholders("${jdbc.hibernate.dialect}"));
        factory.setJpaProperties(jpaProperties);

        factory.afterPropertiesSet();
        factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
        return factory;
    }
	
	/*
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource)
    {
        EntityManagerFactory factory = entityManagerFactory(dataSource).getObject();
        return new JpaTransactionManager(factory);
    }
    
    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator()
    {
        return new HibernateExceptionTranslator();
    }
    
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(final SessionFactory sessionFactory) {
        final HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);

        return txManager;
    }
    
    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) 
    {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        String dbInitializationEnabled = env.resolvePlaceholders("${jdbc.init-db}");
        dataSourceInitializer.setEnabled(Boolean.parseBoolean(dbInitializationEnabled));
        dataSourceInitializer.setDataSource(dataSource);
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        String dbInitScriptLocation = env.resolvePlaceholders("${jdbc.initLocation}");
        if(StringUtils.isNotEmpty(dbInitScriptLocation))
        {
            databasePopulator.setScripts(new Resource[]{
                    new ClassPathResource(dbInitScriptLocation)
            });
        }
        dataSourceInitializer.setDatabasePopulator(databasePopulator);
        return dataSourceInitializer;
    }
    
   	@Bean
   	public DataSource dataSource()
   	{
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setDriverClassName(env.resolvePlaceholders("${jdbc.driverClassName}"));
      dataSource.setUrl(env.resolvePlaceholders("${jdbc.url}"));
      dataSource.setUsername(env.resolvePlaceholders("${jdbc.username}"));
      dataSource.setPassword(env.resolvePlaceholders("${jdbc.password}"));
      return dataSource;
    }
   	*/
   	
   	/**
   	 * 
   	 */
   	@Bean
    public LocalSessionFactoryBean sessionFactory() {
        final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { "vn.store.jpa.domain" });
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.resolvePlaceholders("${jdbc.driverClassName}"));
        dataSource.setUrl(env.resolvePlaceholders("${jdbc.url}"));
        dataSource.setUsername(env.resolvePlaceholders("${jdbc.username}"));
        dataSource.setPassword(env.resolvePlaceholders("${jdbc.password}"));

        return dataSource;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(final SessionFactory sessionFactory) {
        final HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);

        return txManager;
    }

//    @Bean
//    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
//        return new PersistenceExceptionTranslationPostProcessor();
//    }

    final Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
//        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        hibernateProperties.setProperty("hibernate.dialect", env.resolvePlaceholders("${jdbc.hibernate.dialect}"));
        
//        System.out.println("hibernate.show_sql: " + env.getProperty("hibernate.show_sql"));
        
//        hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        // hibernateProperties.setProperty("hibernate.globally_quoted_identifiers", "true");
        return hibernateProperties;
    }
   	

}
