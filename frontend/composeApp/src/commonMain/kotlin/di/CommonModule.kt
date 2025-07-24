package di

import data.Repository
import data.repository.RepositoryImpl
import org.koin.dsl.module
import presentation.addfriends.AddFriendsComponent
import presentation.addfriends.DefaultAddFriendsComponent
import presentation.balances.BalancesComponent
import presentation.balances.DefaultBalancesComponent
import presentation.bottomnavigation.BottomNavigationComponent
import presentation.bottomnavigation.DefaultBottomNavigationComponent
import presentation.creategroup.CreateGroupComponent
import presentation.creategroup.DefaultCreateGroupComponent
import presentation.editspends.DefaultEditSpendsComponent
import presentation.editspends.EditSpendsComponent
import presentation.editspends.editspendstab.DefaultEditSpendsTabComponent
import presentation.editspends.editspendstab.EditSpendsTabComponent
import presentation.groupsettings.DefaultGroupSettingsComponent
import presentation.groupsettings.GroupSettingsComponent
import presentation.groupspends.DefaultGroupSpendsComponent
import presentation.groupspends.GroupSpendsComponent
import presentation.home.DefaultHomeComponent
import presentation.home.HomeComponent
import presentation.login.DefaultLoginComponent
import presentation.login.LoginComponent
import presentation.navigation.DefaultRootComponent
import presentation.navigation.RootComponent
import presentation.profile.DefaultProfileComponent
import presentation.profile.ProfileComponent
import presentation.register.DefaultRegisterComponent
import presentation.register.RegisterComponent
import presentation.search.DefaultSearchComponent
import presentation.search.SearchComponent
import presentation.settleup.DefaultSettleUpComponent
import presentation.settleup.SettleUpComponent
import presentation.spenddetails.DefaultSpendDetailsComponent
import presentation.spenddetails.SpendDetailsComponent
import presentation.totals.DefaultTotalsComponent
import presentation.totals.TotalsComponent

val commonModule = module {

    single<Repository> { RepositoryImpl(get(), get()) }

    single<RootComponent.Factory> { DefaultRootComponent.Factory(get()) }
    single<LoginComponent.Factory> { DefaultLoginComponent.Factory(get()) }
    single<RegisterComponent.Factory> { DefaultRegisterComponent.Factory(get()) }
    single<HomeComponent.Factory> { DefaultHomeComponent.Factory(get()) }
    single<ProfileComponent.Factory> { DefaultProfileComponent.Factory(get()) }
    single<CreateGroupComponent.Factory> { DefaultCreateGroupComponent.Factory(get()) }
    single<GroupSpendsComponent.Factory> { DefaultGroupSpendsComponent.Factory(get()) }
    single<SettleUpComponent.Factory> { DefaultSettleUpComponent.Factory(get()) }
    single<TotalsComponent.Factory> { DefaultTotalsComponent.Factory() }
    single<BottomNavigationComponent.Factory> { DefaultBottomNavigationComponent.Factory(get()) }
    single<AddFriendsComponent.Factory> { DefaultAddFriendsComponent.Factory(get()) }
    single<BalancesComponent.Factory> { DefaultBalancesComponent.Factory(get()) }
    single<EditSpendsComponent.Factory> { DefaultEditSpendsComponent.Factory(get()) }
    single<GroupSettingsComponent.Factory> { DefaultGroupSettingsComponent.Factory(get()) }
    single<SearchComponent.Factory> { DefaultSearchComponent.Factory(get()) }
    single<SpendDetailsComponent.Factory> { DefaultSpendDetailsComponent.Factory(get()) }

}