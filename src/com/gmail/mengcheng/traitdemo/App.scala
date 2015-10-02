package com.gmail.mengcheng.traitdemo
/**
 * 定义一个抽象类Animal表示所有的动物，然后定义了两个
 * trait Flyable和Swimable表示会飞和会游泳两种特征
 */
abstract  class Animalx{
  def walk(speed : Int)
  def breathe()={
    println("animal breathes");
  }
}
/**
 * 有两个方法，一个是hasFeather方法，已经实现
 * 另一个是fly方法，只是定义没有实现
 */
trait Flyable{
  def hasFeather=true
  def fly
}
trait Swimable{
  def swim
}
/**
 * 定义一种动物，即会飞也会游泳
 * 继承Animal,后面两个wih
 * 表示具备两种特征
 */
class FishEagle extends Animalx with Flyable with Swimable{
  def walk(speed : Int)=println("fish eagle walk with speed "+speed)
  def swim()=println("fish eagle can swim fast")
  def fly()=println("fish eagle can fly fast")
}

object Application {
  def main(args:Array[String]){
    val fishEagle =new FishEagle
    val flyable: Flyable=fishEagle
    flyable.fly
    
    val swimmer:Swimable=fishEagle
    swimmer.swim
  }
}