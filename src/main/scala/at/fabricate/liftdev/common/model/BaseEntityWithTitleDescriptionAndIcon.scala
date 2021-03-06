package at.fabricate.liftdev.common
package model

import net.liftweb.mapper.LongKeyedMapper

// This is the basic mapper entity type
// especially for use in User (without IdPK)

trait BaseEntityWithTitleDescriptionAndIcon [T <: (BaseEntityWithTitleDescriptionAndIcon[T]) ] extends BaseEntity[T] with BaseEntityWithTitleAndDescription[T] with AddIcon[T]
{
  self: T =>
    
}

trait BaseMetaEntityWithTitleDescriptionAndIcon[ModelType <: ( BaseEntityWithTitleDescriptionAndIcon[ModelType]) ] extends BaseMetaEntity[ModelType] with BaseMetaEntityWithTitleAndDescription[ModelType] with AddIconMeta[ModelType]
{
    self: ModelType  =>
      
}
