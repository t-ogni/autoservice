package com.ktproject.autoservice

import android.util.Log
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ktproject.autoservice.ui.views.*
import com.ktproject.autoservice.ui.views.admin.news.AdminCreateNewsScreen
import com.ktproject.autoservice.ui.views.admin.AdminDashboardScreen
import com.ktproject.autoservice.ui.views.admin.requests.AdminRequestsScreen
import com.ktproject.autoservice.ui.views.admin.user.AdminUsersScreen
import com.ktproject.autoservice.ui.views.admin.news.AdminEditNewsScreen
import com.ktproject.autoservice.ui.views.admin.news.ListNewsScreen
import com.ktproject.autoservice.ui.views.admin.requests.AdminRequestDetailScreen
import com.ktproject.autoservice.ui.views.admin.service.AdminCreateServiceScreen
import com.ktproject.autoservice.ui.views.admin.service.AdminListServicesScreen
import com.ktproject.autoservice.ui.views.admin.service.AdminServiceDetailScreen
import com.ktproject.autoservice.ui.views.admin.user.AdminUserDetailsScreen
import com.ktproject.autoservice.ui.views.login.LoginScreen
import com.ktproject.autoservice.ui.views.login.RegisterScreen
import com.ktproject.autoservice.ui.views.news.NewsDetailScreen
import com.ktproject.autoservice.ui.views.profile.EditProfileScreen
import com.ktproject.autoservice.ui.views.profile.ProfileScreen
import com.ktproject.autoservice.ui.views.request.MyRequestsScreen
import com.ktproject.autoservice.ui.views.request.RequestDetailsScreen
import com.ktproject.autoservice.ui.views.request.request_create.CarInfoCommentScreen
import com.ktproject.autoservice.ui.views.request.request_create.ConfirmRequestScreen
import com.ktproject.autoservice.ui.views.request.request_create.SelectDateTimeScreen
import com.ktproject.autoservice.ui.views.request.request_create.SelectServiceScreen
import com.ktproject.autoservice.ui.views.services.ServiceDetailsScreen
import com.ktproject.autoservice.ui.views.services.ServicesScreen

@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String = "splash_screen") {
    NavHost(navController = navController, startDestination = startDestination) {

        // Сплеш-скрин с проверкой авторизации
        composable("splash_screen") {
            SplashScreen(
                onAuthFailed = {
                    navController.navigate("login")
                },
                onAuthSuccess = {
                    navController.navigate("home")
                }
            )
        }

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
                        popUpTo("register") { inclusive = true }
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
                onRequestClick = { requestId ->
                    navController.navigate("request_details/$requestId")
                },
                onNewsClick = { newsId -> navController.navigate("news_details/$newsId") },
                onAdminPanelClick = { navController.navigate("admin_dashboard") }
            )
        }

        // 📅 Создание заявки (многошаговое)
        composable("create_request/service") {
            SelectServiceScreen(

                onNext = { navController.navigate("create_request/datetime") }
            )
        }
        composable("create_request/service/{serviceId}") { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: ""
            SelectServiceScreen(
                preselectedServiceId = serviceId,
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
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("newRequestCreated", true)

                    navController.popBackStack("home", inclusive = false)
                }
            )
        }

        // 📖 Заявки
        composable("my_requests") {
            MyRequestsScreen(
                onNewRequestClick = { navController.navigate("create_request/service") },
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
            ServiceDetailsScreen(
                serviceId = serviceId,
                onCreateRequest = { usedServiceId ->
                    navController.navigate("create_request/service/$usedServiceId")
                },
                onBackClick = { navController.popBackStack() }
            )
        }


        composable("news_details/{newsId}") { backStackEntry ->
            val newsId = backStackEntry.arguments?.getString("newsId") ?: ""
            NewsDetailScreen(newsId = newsId)
        }

        // 👤 Профиль
        composable("profile") {
            ProfileScreen(
                navController = navController,
                onLogoutSuccess = {
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                },
                onEditProfileClick = {
                    navController.navigate("edit_profile")
                },
                onMyRequestsClick = {
                    navController.navigate("my_requests")
                },
            )
        }
// 👤 Профиль
        composable("edit_profile") {
            EditProfileScreen(
                onProfileUpdated = {
                    navController.navigate("profile") {
                        popUpTo("profile")
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

        composable("admin_requests") {
            AdminRequestsScreen(
                onRequestClick = { requestId ->
                    navController.navigate("admin_request_edit/$requestId")
               }
            )
        }

        composable("admin_request_edit/{requestId}") { backStackEntry ->
            val requestId = backStackEntry.arguments?.getString("requestId") ?: ""
            AdminRequestDetailScreen(requestId = requestId)
        }

        // ⚙️ Admin Panel
        composable("admin_dashboard") {
            AdminDashboardScreen(
                onAllRequestsClick = { navController.navigate("admin_requests") },
                onManageServicesClick = { navController.navigate("admin_services") },
                onManageNewsClick = { navController.navigate("admin_news") },
                onUsersClick = { navController.navigate("admin_users") }
            )
        }

//        // 🛠 Admin Services
        composable("admin_services") {
            AdminListServicesScreen(
                onServiceAddClick = { navController.navigate("admin_services/create") },
                onServiceClick = { serviceId ->
                    navController.navigate("admin_services/edit/$serviceId")
                }
            )
        }
        composable("admin_services/create") {
            AdminCreateServiceScreen(onServiceCreated = { navController.popBackStack() })
        }
        composable("admin_services/edit/{serviceId}") { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: ""
            AdminServiceDetailScreen(serviceId = serviceId, onSave = { navController.popBackStack() })
        }

        // 📰 Admin News
        composable("admin_news") {
            ListNewsScreen(
                onAddNewsClick = { navController.navigate("admin_news/create") },
                onEditNewsClick = { newsId -> navController.navigate("admin_news/edit/$newsId") },
           )
        }
        composable("admin_news/create") {
            AdminCreateNewsScreen(onAddNews = { navController.popBackStack() })
        }
        composable("admin_news/edit/{newsId}") { backStackEntry ->
            val newsId = backStackEntry.arguments?.getString("newsId") ?: ""
            Log.d("AdminEditNewsScreen", "Received newsId = $newsId")
            AdminEditNewsScreen(
                newsId = newsId,
                onCancelClick = { navController.popBackStack() },
                onSaveClick = { navController.popBackStack() }
            )
        }

        // 👥 Admin Users
        composable("admin_users") {
            AdminUsersScreen(
                onUserClick = { userId ->
                    navController.navigate("admin_user_details/$userId")
                }
            )
        }
        composable("admin_user_details/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            AdminUserDetailsScreen(userId = userId)
        }
    }
}
