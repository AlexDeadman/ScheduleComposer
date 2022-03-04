package com.alexdeadman.auddistandroid.data.model

import com.alexdeadman.auddistandroid.data.model.entity.Entity

data class DataList<T : Entity> (var data: List<T>)
