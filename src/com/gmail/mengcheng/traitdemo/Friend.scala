package com.gmail.mengcheng.traitdemo

trait Friend {
  val name : String
  def listen()=println("Your friend "+name + "is listening");
}

class Human(val name : String) extends Friend
class Man(override val name :String) extends Human(name)
class Woman(override val name :String) extends Human(name)



class Animal
class Dog(val name :String) extends Animal with Friend



class Cat(val name :String )extends Animal

object App{
  /**
   * main方法
   */
  def main(args: Array[String]){
    val scat=new Cat("cat") with Friend
    scat.listen()
    
  }
}

