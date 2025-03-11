package com.icare.di

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.util.ResourceUtils
import java.io.FileInputStream
import java.io.IOException
import javax.sql.DataSource


@Configuration
class AppModule {

    @Autowired
    private lateinit var env: Environment

    @Bean
    fun fireBaseInitializing(): FirebaseApp {
        val serviceAccount = FileInputStream(ResourceUtils.getFile("servicekey.json").path)
        val options = FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()
        return FirebaseApp.initializeApp(options)
    }

    @Bean
    fun database(): DataSource {
        val dataSourceBuilder = DataSourceBuilder.create()
        dataSourceBuilder.driverClassName(env.getProperty("spring.datasource.driverClassName")) // Replace if needed based on your driver
        dataSourceBuilder.url(env.getProperty("spring.datasource.url"))
        dataSourceBuilder.username(env.getProperty("spring.datasource.username"))
        dataSourceBuilder.password(env.getProperty("spring.datasource.password"))
        return dataSourceBuilder.build()
    }

    @Bean
    fun iCareJdbcTemplate(database: DataSource?): JdbcTemplate {
        return JdbcTemplate(database!!)
    }

}
