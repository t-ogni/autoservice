package com.ktproject.autoservice.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Вспомогательный объект для управления цветами и текстами статусов заявок
 */
object StatusColors {
    /**
     * Получает цвет для статуса заявки в зависимости от текущей темы
     * @param status строковый идентификатор статуса
     * @return Color соответствующий цвет для отображения
     */
    @Composable
    fun getStatusColor(status: String): Color {
        return when (status) {
            "wait" -> if (isSystemInDarkTheme()) AutoOrangeDark else StatusWait
            "active" -> if (isSystemInDarkTheme()) AutoBlueDark else StatusActive
            "completed" -> if (isSystemInDarkTheme()) AutoLightGreen else StatusCompleted
            "canceled" -> if (isSystemInDarkTheme()) AutoLightRed else StatusCanceled
            else -> MaterialTheme.colorScheme.primary
        }
    }
    
    /**
     * Получает текстовое представление статуса для отображения пользователю
     * @param status строковый идентификатор статуса
     * @return String человекочитаемое представление статуса
     */
    @Composable
    fun getStatusText(status: String): String {
        return when (status) {
            "wait" -> "В ожидании"
            "active" -> "В работе"
            "completed" -> "Выполнено"
            "canceled" -> "Отменено"
            else -> status
        }
    }
    
    /**
     * Получает цвет фона для заявки в зависимости от статуса (с низкой непрозрачностью)
     * @param status строковый идентификатор статуса
     * @param alpha уровень прозрачности (по умолчанию 0.1f)
     * @return Color соответствующий цвет фона
     */
    @Composable
    fun getStatusBackgroundColor(status: String, alpha: Float = 0.1f): Color {
        return getStatusColor(status).copy(alpha = alpha)
    }
    
    /**
     * Проверяет, является ли статус заявки финальным (завершенным или отмененным)
     * @param status строковый идентификатор статуса
     * @return Boolean true, если статус финальный
     */
    fun isFinalStatus(status: String): Boolean {
        return status == "completed" || status == "canceled"
    }
    
    /**
     * Проверяет, является ли статус заявки активным (в работе)
     * @param status строковый идентификатор статуса
     * @return Boolean true, если заявка активна
     */
    fun isActiveStatus(status: String): Boolean {
        return status == "active"
    }
    
    /**
     * Получает значок (иконку) для статуса заявки
     * @param status строковый идентификатор статуса
     * @return String имя иконки статуса
     */
    fun getStatusIcon(status: String): String {
        return when (status) {
            "wait" -> "ClockIcon"
            "active" -> "BuildIcon"
            "completed" -> "CheckCircleIcon"
            "canceled" -> "CancelIcon"
            else -> "HelpIcon"
        }
    }
}
