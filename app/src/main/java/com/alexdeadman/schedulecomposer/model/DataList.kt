package com.alexdeadman.schedulecomposer.model

import com.alexdeadman.schedulecomposer.model.entity.Attributes
import com.alexdeadman.schedulecomposer.model.entity.Entity

data class DataList<T : Entity<out Attributes>> (var data: List<T>)
