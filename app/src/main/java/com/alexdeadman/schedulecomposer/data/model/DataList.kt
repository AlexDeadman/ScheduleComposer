package com.alexdeadman.schedulecomposer.data.model

import com.alexdeadman.schedulecomposer.data.model.entity.Attributes
import com.alexdeadman.schedulecomposer.data.model.entity.Entity
import com.alexdeadman.schedulecomposer.data.model.entity.Relationships

data class DataList<T : Entity<out Attributes, out Relationships>> (var data: List<T>)
