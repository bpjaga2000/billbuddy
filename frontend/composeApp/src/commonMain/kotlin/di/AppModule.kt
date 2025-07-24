package di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.mp.KoinPlatform


val koin by lazy { KoinPlatform.getKoinOrNull() ?: initKoin().koin }

fun initKoin(
    appDeclaration: KoinAppDeclaration? = null
) = startKoin {
    appDeclaration?.invoke(this)
    modules(platformModule + commonModule)
}