package com.alexdeadman.schedulecomposer.model

import com.alexdeadman.schedulecomposer.model.entity.Attributes
import com.alexdeadman.schedulecomposer.model.entity.Entity
import com.alexdeadman.schedulecomposer.model.entity.Relationships

data class DataList<T : Entity<out Attributes, out Relationships>> (var data: List<T>)
