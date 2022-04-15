package com.alexdeadman.schedulecomposer.model

import com.alexdeadman.schedulecomposer.model.entity.Attributes
import com.alexdeadman.schedulecomposer.model.entity.Entity

data class Data<T : Entity<out Attributes>> (var data: T)
