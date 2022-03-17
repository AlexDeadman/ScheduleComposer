package com.alexdeadman.auddistandroid.data.model

import com.alexdeadman.auddistandroid.data.model.entity.Attributes
import com.alexdeadman.auddistandroid.data.model.entity.Entity
import com.alexdeadman.auddistandroid.data.model.entity.Relationships

data class DataList<T : Entity<out Attributes, out Relationships>> (var data: List<T>)
