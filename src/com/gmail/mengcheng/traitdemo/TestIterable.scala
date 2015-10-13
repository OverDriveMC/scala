package com.gmail.mengcheng.traitdemo

import scala.collection.immutable.SortedSet
import scala.collection.mutable.LinkedList
import scala.collection.mutable.Map
import scala.io._
import scala.math._
import scala.collection.JavaConversions._

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
    testScanLeft
    testZippers
    //testStream
    testLazyView
    testExWithJava
    concurrent
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

  def testScanLeft={
    val s=(1 to 10).scanLeft(0)(_ + _)
    println(s)
  }

  def testZippers={
    val prices=List(5.0,20.0,9.95)
    val quantities=List(10,2,1)
    //zip方法让你将他们组合成一个个对偶的列表
    val res=prices zip quantities
    //将得到一个List[(Double,Int)]
    println(res)
    //这样一来对每个对偶应用函数就很容易了
    val app=(prices zip quantities) map {p=> p._1 *p._2}
    //结果是一个包含了价格的列表
    println(app)
    //所有物品的总价就是
    val totalSum=(((prices zip quantities) map {p=>p._1*p._2}) sum)
    println(totalSum)


    val xs=List(1,2,3)
    val ys=List("A","B")
    val zs=List("①","②","③","④")
    val x=0
    val y="_"
    //x为前面缺省值，y为后面缺省值
    println(xs.zipAll(ys, x, y)) //List((1,A), (2,B), (3,_))
    println(xs.zipAll(zs,x,y))//List((1,①), (2,②), (3,③), (0,④))
    //Vector((S,0), (c,1), (a,2), (l,3), (a,4))
    println("Scala".zipWithIndex)
    //(l,3)
    println("Scala".zipWithIndex.max)
    //3
    println("Scala".zipWithIndex.max._2)
  }

  def numsFrom(n: BigInt) : Stream[BigInt]=n #:: numsFrom(n+1)
  //#::操作符很像是列表的::操作符，只不过他构筑出来的是一个流
  def testStream={
    val tenOrMore=numsFrom(10)
    //Stream(10, ?) 流对象，但其尾部是未被求值得
    println(tenOrMore)
    //Stream(13, ?)
    println(tenOrMore.tail.tail.tail)
    //流的方法是懒执行的
    val squares=numsFrom(1).map { x => x*x }
    //Stream(1, ?) 需要调用squares.tail来强制对下一个元素求值
    println(squares)
    //如果想得到多个答案，则可以调用take，然后用force，这将强制对所有值求值
    //Stream(1, 4, 9, 16, 25)
    println(squares.take(5).force)
    //不要尝试调用squares.force 这个调用将尝试对一个无穷流的所有成员进行求值，
    //引发OutOfMemoryError

    //可以从迭代器构造一个流，举例来说:Source.getLines方法返回一个Iterator[String]
    //用这个迭代器，对于每一行只能访问一次，而流将缓存访问过的行，允许你重新访问他们
    val words=Source.fromFile("F:/1.txt").getLines.toStream
    //Stream(avsflsfslh, ?)
    println(words)
    //fsdfvsdfv
    println(words(3))
    //Stream(avsflsfslh, sdfasdfas, fsadfasdfsd, fsdfvsdfv, ?)
    println(words)
  }
  def testLazyView={
    val powers=(0 until 1000).view.map(pow(10,_))
    //将产出一个未被求值的集合(不像流，连第一个元素都未被求值)
    println(powers)
    //pow(10,100)被计算，但其他值的幂并没有被计算，和流不同，这些视图
    //并不缓存任何值，如果再次调用powers(100)，pow(10,100)将会被重新计算
    println(powers(100))
    //和流一样，用force方法可以对懒视图强制求值，你将得到与原集合相同类型的新集合
    //懒集合对于处理那种需要以多种方式进行变换的大型集合是很有好处的，因为它避免了构建出大行中间集合的需要
    /**
     * 前一个将会计算出10的n次方的集合，在对每一个得到的数取倒数
     * 而后一个产出的是记住了两个map操作的视图，当求值动作被强制执行时，对于每个元素
     * 这两个操作被同时执行，不需要额外构建中间集合
     */
    println((0 to 1000).map(pow(10,_)).map(1 / _))
    println((0 to 1000).view.map(pow(10,_)).map(1 / _))

  }


  def testExWithJava={
    //给目标值显示地指定一个类型来触发转换
    val props : Map[String,String]=System.getProperties()
    //调用底层Properties对象的put("com.hrstmann.scala","impatient")
    props("com.horstmann.scala")="impatient"
    println(props)
  }

  def  synchronized={
    val scores=new scala.collection.mutable.HashMap[String,Int] with
      scala.collection.mutable.SynchronizedMap[String,Int]
  }

  def concurrent={
    //可以通过对要遍历的集合应用.par并行化for循环
    //数字是按照作用于该任务的线程产出的顺序输出的
    var sum=0
     //until不算100  to的话加到100
    for( i<-(0 until 100).par){
      print(i+" ")
      //sum结果不可预知
      sum+=i
    }
    println("\n"+sum)
    //而在for/yield循环中，结果是依次组装的
    for(i<-(0 until 100).par)
      yield print(i+" ")
    
    var str="Hello World"
    println()
    println(str.foldLeft(Set[Char]())(_+_))
    //++和--用于批量添加和移除元素
    println(str.par.aggregate(Set[Char]())(_+_,_++_))
  }
}
