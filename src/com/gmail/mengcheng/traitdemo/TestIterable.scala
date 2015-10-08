package com.gmail.mengcheng.traitdemo

import scala.collection.immutable.SortedSet
import scala.collection.mutable.LinkedList
import scala.collection.mutable.Map

object Color extends Enumeration{
    val RED=0xFF0000
    val GREEN=0xFF00
    val BLUE=0xFF
}

object TestIterable {
  def main(args : Array[String]){
    Iterable(0xFF,0xFF00,0xFF0000)
    Set(Color.RED,Color.GREEN,Color.BLUE)
    Map(Color.RED -> 0xFF0000, Color.GREEN->0xFF00,Color.BLUE->0xFF)
    val sortedSet=SortedSet("Hello" , "World") 
    
    testMap()
    testCollect()
    testReduce()
    testCollapse
  }
  def sum(lst: List[Int]):Int={
    if(lst==Nil) 0
    else lst.head+sum(lst.tail)
  }
  
  def sum2(lst : List[Int]):Int =lst match{
    case Nil =>0
    case h::t =>h+sum(t)
  }
  
  def mutableList()={
    val lst=LinkedList(1,-2,7,-9)
    var cur=lst
    //所有负值变为0
    while(cur!=Nil){
      if(cur.elem<0) cur.elem=0
      cur=cur.next
    }
    cur=lst;
    //去除每两个元素中的一个
    while(cur!=Nil && cur.next!=Nil){
      cur.next=cur.next.next
      cur=cur.next
    }
  }
  
  def ulcase(s:String)={
    Vector(s.toUpperCase(),s.toLowerCase())
  }
  def testMap()={
    val names=List("Peter","Paul","Mary")
    println(names.map(ulcase))
    println(names.flatMap(ulcase))
    names.foreach(println)
  }
  //vec:Vector(-1, 1, 1, 1)
  def testCollect()={
    val vec= "-3+4++".collect{
      case '+' =>1;
      case '-' => -1
    }
    println("vec:"+vec)
    
  }
  
  def testReduce()={
    println(List(1,7,2,9).reduceLeft(_-_)) //-17
    println(List(1,7,2,9).reduceRight(_-_)) //-13
    println(List(1,7,2,9).foldLeft("")(_+_)) //1729
    println((0/:List(1,7,2,9))(_-_))   //-19
  }
  
  
  def testCollapse={
    println("testCollapse:")
    var freq=Map[Char,Int]()
    for(c<-"Mississippi")
      freq(c)=freq.getOrElse(c,0)+1
    //Map(M -> 1, s -> 4, p -> 2, i -> 4)
     println(freq)
     freq.foreach(e=>println(e._1+"="+e._2))
     //Map(M -> 1, s -> 4, p -> 2, i -> 4)
     val freqc=(Map[Char,Int]() /:"Mississippi"){
      (m,c) =>m+(c -> (m.getOrElse(c, 0)+1))
     }
     println(freqc)
     
  }
}