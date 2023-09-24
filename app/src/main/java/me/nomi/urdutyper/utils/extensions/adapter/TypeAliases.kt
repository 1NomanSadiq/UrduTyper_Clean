package me.nomi.urdutyper.utils.extensions.adapter

import androidx.recyclerview.widget.DiffUtil

typealias DiffUtilMaker<T> = () -> DiffUtil.ItemCallback<T>