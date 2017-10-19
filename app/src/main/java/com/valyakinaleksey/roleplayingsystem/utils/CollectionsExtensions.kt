@file:JvmName("CollectionExtensions")
package com.valyakinaleksey.roleplayingsystem.utils


fun <E> Iterable<E>.makeList(): MutableList<E> {
  val list = ArrayList<E>()
  list += this
  return list
}