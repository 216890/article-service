package pl.lodz.p.stanczyk.articleservice.config

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.lodz.p.stanczyk.articleservice.adapter.MongoArticleRepositoryAdapter
import pl.lodz.p.stanczyk.articleservice.domain.ArticleService
import pl.lodz.p.stanczyk.articleservice.domain.port.ArticleRepository
import pl.lodz.p.stanczyk.articleservice.infrastructure.InstantNowSupplier
import pl.lodz.p.stanczyk.articleservice.infrastructure.InstantNowSupplierImpl
import pl.lodz.p.stanczyk.articleservice.infrastructure.mongo.MongoArticleRepository

@Configuration
class ApplicationConfig {
    @Bean
    fun articleService(mongoUserRepository: MongoArticleRepository) = ArticleService(
        articleRepository = articleRepository(mongoUserRepository),
        instantNowSupplier = instantNowSupplier()
    )

    @Bean
    fun articleRepository(mongoUserRepository: MongoArticleRepository): ArticleRepository =
        MongoArticleRepositoryAdapter(
            mongoRepository = mongoUserRepository
        )

    @Bean
    fun instantNowSupplier(): InstantNowSupplier = InstantNowSupplierImpl()

    @Bean
    fun metricsCommonTags(@Value("spring.application.name") applicationName: String): MeterRegistryCustomizer<MeterRegistry> =
        MeterRegistryCustomizer { registry: MeterRegistry ->
            registry.config().commonTags("application", applicationName)
        }
}
