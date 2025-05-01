package com.ktproject.autoservice.data.repository.in_memory

import com.ktproject.autoservice.ui.viewmodel.NewRequestData

class NewRequestDraftRepository {

    private var draft = NewRequestData()

    fun getDraft(): NewRequestData = draft

    fun updateDraft(data: NewRequestData) {
        draft = data
    }

    fun clearDraft() {
        draft = NewRequestData()
    }
}
