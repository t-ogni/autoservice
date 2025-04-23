package com.ktproject.autoservice

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ktproject.autoservice.ui.views.*
import com.ktproject.autoservice.ui.views.admin.AdminCreateNewsScreen
import com.ktproject.autoservice.ui.views.admin.AdminDashboardScreen
import com.ktproject.autoservice.ui.views.admin.AdminRequestsScreen
import com.ktproject.autoservice.ui.views.admin.AdminServicesScreen
import com.ktproject.autoservice.ui.views.admin.AdminUsersScreen
import com.ktproject.autoservice.ui.views.login.LoginScreen
import com.ktproject.autoservice.ui.views.login.RegisterScreen
import com.ktproject.autoservice.ui.views.news.NewsDetailScreen
import com.ktproject.autoservice.ui.views.news.NewsScreen
import com.ktproject.autoservice.ui.views.request_create.CarInfoCommentScreen
import com.ktproject.autoservice.ui.views.request_create.ConfirmRequestScreen
import com.ktproject.autoservice.ui.views.request_create.SelectDateTimeScreen
import com.ktproject.autoservice.ui.views.request_create.SelectServiceScreen

@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String = "login") {
    NavHost(navController = navController, startDestination = startDestination) {

        // 🔐 Аутентификация
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home"){
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("home"){
                        popUpTo("login") { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // 🏠 Главная
        composable("home") {
            HomeScreen(
                navController,
                onCreateRequestClick = { navController.navigate("create_request/service") },
                onMyRequestsClick = { navController.navigate("my_requests") },
                onServicesClick = { navController.navigate("services") },
                onNewsClick = { navController.navigate("news") },
                onProfileClick = { navController.navigate("profile") },
                onAdminPanelClick = { navController.navigate("admin_dashboard") }
            )
        }

        // 📅 Создание заявки (многошаговое)
        composable("create_request/service") {
            SelectServiceScreen(
                onNext = { navController.navigate("create_request/datetime") }
            )
        }

        composable("create_request/datetime") {
            SelectDateTimeScreen(
                onNext = { navController.navigate("create_request/car_comment") }
            )
        }

        composable("create_request/car_comment") {
            CarInfoCommentScreen(
                onNext = { navController.navigate("create_request/confirm") }
            )
        }

        composable("create_request/confirm") {
            ConfirmRequestScreen(
                onSubmit = {
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }

        // 📖 Заявки
        composable("my_requests") {
            MyRequestsScreen(
                onRequestClick = { requestId ->
                    navController.navigate("request_details/$requestId")
                }
            )
        }

        composable("request_details/{requestId}") { backStackEntry ->
            val requestId = backStackEntry.arguments?.getString("requestId") ?: ""
            RequestDetailsScreen(requestId = requestId)
        }

        // 🛠 Услуги
        composable("services") {
            ServicesScreen(
                navController,
                onServiceClick = { serviceId ->
                    navController.navigate("service_details/$serviceId")
                }
            )
        }

        composable("service_details/{serviceId}") { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: ""
            ServiceDetailsScreen(serviceId = serviceId)
        }

        // 📰 Новости
        composable("news") {
            NewsScreen(
                onNewsClick = { newsId ->
                    navController.navigate("news_details/$newsId")
                }
            )
        }

        composable("news_details/{newsId}") { backStackEntry ->
            val newsId = backStackEntry.arguments?.getString("newsId") ?: ""
            NewsDetailScreen(newsId = newsId)
        }

        // 👤 Профиль
        composable("profile") {
            ProfileScreen(
                navController,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        // 🔧 Админка
        composable("admin_dashboard") {
            AdminDashboardScreen(
                onAllRequestsClick = { navController.navigate("admin_requests") },
                onManageServicesClick = { navController.navigate("admin_services") },
                onManageNewsClick = { navController.navigate("admin_news") },
                onUsersClick = { navController.navigate("admin_users") }
            )
        }

        composable("admin_requests") { AdminRequestsScreen() }
        composable("admin_services") { AdminServicesScreen() }
        composable("admin_news") { AdminCreateNewsScreen() }
        composable("admin_users") { AdminUsersScreen() }
    }
}
