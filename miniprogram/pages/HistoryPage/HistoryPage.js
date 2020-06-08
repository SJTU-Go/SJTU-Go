//index.js
const app = getApp()
Page({
  data:{
    orderList:[],
    routeList:[],
    planList:[]
  },

  onLoad:function(options){
    var that=this
   //var list=new Array()
    // 页面初始化 options为页面跳转所带来的参数
    wx.getStorage({
      key: 'history',
      success:function(res){
        console.log(res.data)
        that.setData({orderList:res.data})
      }
    })
    wx.getStorage({
      key: 'historygained',
      success:function(res){
        var wordlist=new Array(0);
        console.log("transforming")
        var i = 0
        while (i<res.data.length){
        console.log(res.data[i].strategy.depart)
        console.log(res.data[i].strategy.travelTime)
        console.log(res.data[i].strategy.arrive)
        console.log(res.data[i].strategy.pass)
        var historyword = {}
        historyword.depart = res.data[i].strategy.depart
        historyword.arrive = res.data[i].strategy.arrive
        historyword.pass = res.data[i].strategy.pass
        historyword.routetime =res.data[i].strategy.travelTime
        historyword.distance = res.data[i].strategy.distance
        historyword.type = res.data[i].strategy.type
          i = i+1
          wordlist.push(historyword)
      }
      wx.setStorage({
        data: wordlist,
        key: 'history',
      })
      var q= 0 
      var linelist = new Array(0)
      while (q<res.data.length){
    var d = res.data[q].strategy.routeplan
    var polyline=new Array(0)
    for(var j=0;j<d.length;j++){
      var line = {};
      var points = [];
      var item = d[j];
      for(var i of item.routePath.coordinates){
        var cor = {};
        cor['longitude']=i[0];
        cor['latitude']=i[1];
        points.push(cor);
      }
      if(item.type=="HELLOBIKE"){
        line['color']='#0099FF';
      }
      if(item.type=="FIND"){
        line['color']='#FFCC33';
      }
      else{line['color']='#00CC33'}
      line['points']=points;
      //line['color']='#808080';
      line['width']=4;
      //line['dottedLine']=true;
      //console.log(line);
      polyline.push(line);
    }
    console.log(polyline)
    q = q+1
    console.log("routing")
console.log(linelist)
    linelist.push(polyline)

}

wx.setStorage({
  data:linelist,
  key: 'historyroute',
})

      }
    })
    /*
    for(var j=0;j<d.length;j++){
      var line = {};
      var points = [];
      var item = d[j];
      for(var i of item.routePath.coordinates){
        var cor = {};
        cor['longitude']=i[0];
        cor['latitude']=i[1];
        points.push(cor);
      }
      if(item.type=="HELLOBIKE"){
        line['color']='#0099FF';
      }
      if(item.type=="FIND"){
        line['color']='#FFCC33';
      }
      else{line['color']='#00CC33'}
      line['points']=points;
      //line['color']='#808080';
      line['width']=4;
      //line['dottedLine']=true;
      //console.log(line);
      polyline.push(line);
    }
    //console.log(polyline)
    this.setData({
      polyline:polyline
    })
*/



    wx.getStorage({
      key: 'historyroute',
      success:function(res){
        console.log(res.data)
        that.setData({routeList:res.data})
      }
    })
    wx.getStorage({
      key: 'plan',
      success:function(res){
        console.log(res.data)
        that.setData({planList:res.data})
      }
    })
    //console.log(that.data.orderList)

    //this.setData({
      //  orderList:[{
      //   arrive:'菁菁堂',
      //   depart:'D24宿舍楼',
      //   passPlaces:'第四餐饮大楼',
      //   routetime:'20min',
      //  },{
      //   arrive:'霍英东体育馆',
      //   depart:'上院',
      //   passPlaces:'无',
      //   routetime:'13min',
      //  }]
   // })
  },

  searchPage:function(e){
    console.log(e)
    var index=e.currentTarget.dataset.index
    console.log(this.data.routeList)
    var route = this.data.routeList[index]
    //var route = 'routeList['+index+']'
    console.log(route)
   wx.navigateTo({ url: '../feedback/feedback?RT='+JSON.stringify(route),})



},
indexback:function()
{
  wx.switchTab(
    {
  url: '../index/index'}
  )},

  onReady:function(){
    // 页面渲染完成
  },
  onShow:function(){
    // 页面显示
  },
  onHide:function(){
    // 页面隐藏
  },
  onUnload:function(){
    // 页面关闭
  },
  showRoute:function(e){
    console.log(e)
    var index=e.currentTarget.dataset.index
    //console.log(this.data.routeList)
    var route = this.data.routeList[index]
    var plan = this.data.planList[index]
    //var route = 'routeList['+index+']'
    console.log(route)
    console.log(plan)
  /* wx.navigateTo({ url: '../commentmap/commentmap?RT='+JSON.stringify(route)+'&plan='+JSON.stringify(plan),})*/
  wx.navigateTo({ url: '../commentmap/commentmap?index='+index})
  },


})

