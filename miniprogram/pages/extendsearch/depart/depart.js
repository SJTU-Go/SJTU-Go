var app = getApp()
var x_PI = 3.14159265358979324 * 3000.0 / 180.0;

var PI = 3.1415926535897932384626;

var a = 6378245.0;

var ee = 0.00669342162296594323;
Page({

  data: {
    coordinateview:false,
    id:0,
    latitude: 31.021807,//31.029236,
    longitude: 121.429846,//121.452591,
    hasmarkers:true,
    markers:new Array(0),
    boxshow:true,
    datapass:'',
    // 搜索框状态
    inputShowed: true,
    //显示结果view的状态
    viewShowed: false,
    // 搜索框值
    inputVal: "",
    //搜索渲染推荐数据
    catList: [],

    btnWidth: 300, //删除按钮的宽度单位
    startX: "", //收支触摸开始滑动的位置
    inputmessage:"输入出发点",
  },
  chooselocate:function()
  {var cor = this.data.coordinateview
    this.setData({coordinateview:true})
    var llatitude = this.data.latitude
    var llongitude = this.data.longitude
    var q = []
    q.push({iconPath: "../../../images/crosses.png",
    id:1,
    latitude:     llatitude,
    longitude:llongitude,
    width: 40,
    height: 40,
    name:'',
    bikeCount:''})   
    this.setData({markers:q})
  },

  chooseback:function()
  { this.setData({coordinateview:false})
    this.setData({markers:[]});
    this.setData({inputVal:""})
  },

    chooseclick:function()
  {this.searchout()},
  
  returnresult:function(e){
    var  ress ={}
    console.log(e)
    var res
    for(res in this.data.markers){
  if (e.markerId==this.data.markers[res].id){
    this.setData({inputVal:this.data.markers[res].name,
      id:this.data.markers[res].id,
      boxshow:false,
    hasmarkers:false,})
      ress.id = this.data.markers[res].id
      ress.name = this.data.markers[res].name
      console.log(ress)
      wx.setStorage({
        key: 'depart',
        data: ress,
      })}}
      this.searchout()
  },
  onLoad: function () {
    var that = this;
    //初始化界面
    that.initEleWidth();
  },

  // 隐藏搜索框样式
  hideInput: function () {
    this.setData({
      inputVal: "",
      inputShowed: false
    });
  },
  inputreturn:function(event)
  { 
    this.setData({inputVal:event.currentTarget.dataset.name,
    boxshow:false,
    id:event.currentTarget.dataset.id,
  })
  this.searchout()
    wx.setStorage({
    key: 'depart',
    data: event.currentTarget.dataset,
  })
  },
  searchout:function()
  { 
  if (this.data.inputVal.substring(0, 4)=='POIN'){console.log("corrdinating!!")
wx.setStorage({
  data:{name:this.data.inputVal,id:"404"},
  key: 'depart',
})
}
//    wx.navigateTo({url: '../../searchindex/searchindex',})

let pages = getCurrentPages(); //获取当前页面js里面的pages里的所有信息。
 
let prevPage = pages[ pages.length - 2 ];  
 
//prevPage 是获取上一个页面的js里面的pages的所有信息。 -2 是上一个页面，-3是上上个页面以此类推。
if (this.data.inputVal.substring(0, 4)=='POIN'){
prevPage.setData({  // 将我们想要传递的参数在这里直接setData。上个页面就会执行这里的操作。
 
    depart:this.data.inputVal,
    departid:"DT404"

})}
else{console.log("findingid")
console.log(this.data.id)
  prevPage.setData({  // 将我们想要传递的参数在这里直接setData。上个页面就会执行这里的操作。
 
  depart:this.data.inputVal,
  departid:'DT'+this.data.id,

})}


wx.navigateBack({
 
  delta: 1  // 返回上一级页面。

})  


},
  // 清除搜索框值
  clearInput: function () {
    this.setData({
      inputVal: "",
      boxshow:false
    });
  },
  // 键盘抬起事件2
  inputTyping: function (e) {
    this.setData({
      boxshow:true
    })
    console.log(e.detail.value)
    var that = this;
    if (e.detail.value == ''){
        return;
    }
    that.setData({
      viewShowed: false,
      inputVal: e.detail.value
    });
    console.log(e.detail.value)
    wx.request({
      url: 'https://api.ltzhou.com/map/search/destination',
      data: { keyword:e.detail.value} ,
      method: 'GET',
      header: {
        'Content-type': 'application/json'
      },

      success: function (res) {
        var x
        var markers=new Array(0)
        for (x in res.data)
        {

        var marker ={iconPath: "/mark/19.PNG",
          latitude: 31.021807,//31.029236,
          longitude: 121.429846,//121.452591,
          id:0,
          name:'',
          width: 50,
          height: 50,
          bikeCount:''}
          console.log("resing")
          console.log(res.data[x])
            marker.latitude=res.data[x].location.coordinates[1]
            marker.longitude=res.data[x].location.coordinates[0] 
            marker.name=res.data[x].placeName
            marker.iconPath = "../../../images/logo.png"
            marker.id = res.data[x].placeID
            console.log(marker)
            markers.push(marker) 
            console.log("adding")
            console.log(markers)
           }   

 
            
        console.log(res.data)
        that.setData({
          carList: res.data
        })
        that.setData({
          markers:markers
        })
        that.setData({hasmarkers:true})
        console.log("markers")
        console.log(that.data.markers)
      }
    });
  },
  SearchForPoint: function (e) {
    var llat = e.latitude
    var llon = e.longitude
    this.setData({
      boxshow:true
    })
    var that = this;
    that.setData({
      viewShowed: false,
    });
    wx.request({
      url: 'https://api.ltzhou.com/map/nearby/destination',
      data: {lat:llat,lng:llon} ,
      method: 'GET',
      header: {
        'Content-type': 'application/json'
      },
      success: function (res) {
        var x
        var markers=new Array(0)
  
        console.log(res.data)
        that.setData({
          carList: res.data
        })

      }
    });
  },


  // 获取选中推荐列表中的值
  btn_name: function (res) {
     console.log(res.currentTarget.dataset.index, res.currentTarget.dataset.name);
    console.log(res.currentTarget.dataset.index, res.currentTarget.dataset.id);

    var that = this;

    that.hideInput();

    that.setData({
      viewShowed: true,
      carNum: res.currentTarget.dataset.name,
      deviceId: res.currentTarget.dataset.id
    });
  },

// index-serch
  //滑动效果
  touchS: function (e) {
    if (e.touches.length == 1) { //触摸屏上只有一个触摸点
      this.setData({
        //设置触摸起始点水平方向位置
        //clientX:距离页面可显示区域（屏幕除去导航条）左上角距离，横向为X轴，纵向为Y轴
        startX: e.touches[0].clientX
      });
    }
  },
  touchM: function (e) {
    if (e.touches.length == 1) { // 一个触摸点
      //手指移动时水平方向位置
      var moveX = e.touches[0].clientX;
      //手指起始点位置与移动期间的差值
      var disX = this.data.startX - moveX;
      //按钮
      var btnWidth = this.data.btnWidth;
      var txtStyle = "";
      if (disX == 0 || disX < 0) { //如果移动距离小于等于0，说明向右滑动，文本层位置不变
        txtStyle = "left:0px";
      } else if (disX > 0) { //移动距离大于0，文本层left值等于手指移动距离
        txtStyle = "left:-" + disX + "px";
        if (disX >= btnWidth) {
          //控制手指移动距离最大值为删除按钮的宽度
          txtStyle = "left:-" + btnWidth + "px";
        }
      }
      //获取手指触摸的是哪一项
      var index = e.currentTarget.dataset.index;
      //设置该项向左偏移的样式,并消除其他项的偏移样式
      var list = this.data.carList;
      for (var ix in list) {
        ix == index ? list[ix].txtStyle = txtStyle : list[ix].txtStyle = "";
      }
      //更新列表的状态
      this.setData({
        carList: list
      });
    }
  },
  touchE: function (e) {
    if (e.changedTouches.length == 1) { //一个触摸点
      //手指移动结束后水平位置
      var endX = e.changedTouches[0].clientX;
      //触摸开始与结束，手指移动的距离
      var disX = this.data.startX - endX;
      var btnWidth = this.data.btnWidth;
      //如果距离小于删除按钮的1/2，不显示删除按钮
      var txtStyle = disX > btnWidth / 2 ? "left:-" + btnWidth + "px" : "left:0px";
      //获取手指触摸的是哪一项
      var index = e.currentTarget.dataset.index;
      //设置偏移的样式
      var list = this.data.carList;
      list[index].txtStyle = txtStyle;
      //更新列表的状态
      this.setData({
        carList: list
      });
    }
  },
  //获取元素自适应后的实际宽度
  getEleWidth: function (w) {
    var real = 0;
    try {
      var res = wx.getSystemInfoSync().windowWidth;
      var scale = (750 / 2) / (w / 2); //以宽度750px设计稿做宽度的自适应
      real = Math.floor(res / scale);
      return real;
    } catch (e) {
      return false;
      // Do something when catch error
    }
  },
  initEleWidth: function () {
    var btnWidth = this.getEleWidth(this.data.btnWidth);
    this.setData({
      btnWidth: btnWidth
    });
  },
  //点击解绑

  //点击试驾
  tryDriver: function (e) {
    var that = this;
    var index = e.currentTarget.dataset.index;
    var list = that.data.carList;
    if (list[index].state == 5)
      return;

    wx.showModal({
      title: '提示',
      content: '是否确认试驾',
      success(res) {
        if (res.confirm) {
          //试驾
          wx.request({
            url: app.globalData.root + "car/driver.do",
            data: {
              "openid": app.globalData.openid,
              "carNum": e.currentTarget.dataset.name
            },
            method: 'GET',
            header: {
              'Content-type': 'application/json'
            },
            success: function (res) {
              if (res.data == 0) {
                wx.showToast({
                  title: '车辆维修尚未结束、不可试驾',
                  icon: 'none',
                })
                return;
              }
              //切换图标
              for (var ix in list) {
                if (ix == index)
                  list[ix].state = 5;
              }
              //更新列表的状态
              that.setData({
                carList: list
              });
            }
          });
        }
      }
    })
  },
  //事件处理函数
  navmap: function (e) {
    wx.navigateTo({
      url: '../site/site?deviceId=' + e.currentTarget.dataset.id
    })
  },
  //显示车辆状态
  carState: function (e) {
    wx.navigateTo({
      url: '../state/state?carNum=' + e.currentTarget.dataset.car
    })
  },

  regionchange:function(e){
    var con = this.data.coordinateview
    if (con){
    var llatitude
    var llongitude
    console.log(e)

            var that = this;
      
            this.mapCtx = wx.createMapContext("map4select");
      
            this.mapCtx.getCenterLocation({
      
              type: 'gcj02',
      
              success: function(res) {
        that.SearchForPoint(res)
      console.log('location')
      console.log(that.data)
                console.log(res, 11111)
      
                var coordinate = that.gcj02towgs84(res.longitude, res.latitude)
      
                console.log(coordinate, 2222)
      console.log(res.latitude)
      llatitude = res.latitude
      llongitude = res.longitude
      var q = []
      q.push({iconPath: "../../../images/crosses.png",
      id:1,
      latitude:     llatitude,
      longitude:llongitude,
      width: 40,
      height: 40,
      name:'',
      bikeCount:''})   
      that.setData({markers:q})
      var  inputVal="POINT("+llongitude.toFixed(4)+" "+llatitude.toFixed(4)+")"
      that.setData({inputVal:inputVal})

                that.setData({
      
                  latitude: res.latitude,
      
                  longitude: res.longitude,
      
                  circles: [{
      
                    latitude: res.latitude,
      
                    longitude: res.longitude,
      
                    color: '#FF0000DD',
      
                    fillColor: '#d1edff88',
      
                    radius: 0, //定位点半径
      
                    strokeWidth: 10000
      
                  }]
      
                })
   
              }
      
            })}


        },

        gcj02towgs84(lng, lat) {

            var that  = this;
        
            if (that.out_of_china(lng, lat)) {
        
              return [lng, lat]
        
            } else {
        
              var dlat = that.transformlat(lng - 105.0, lat - 35.0);
        
              var dlng = that.transformlng(lng - 105.0, lat - 35.0);
        
              var radlat = lat / 180.0 * PI;
        
              var magic = Math.sin(radlat);
        
              magic = 1 - ee * magic * magic;
        
              var sqrtmagic = Math.sqrt(magic);
        
              dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
        
              dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
        
              var mglat = lat + dlat;
        
              var mglng = lng + dlng;
        
              return [lng * 2 - mglng, lat * 2 - mglat]
        
            }
        
          },
          transformlat(lng, lat) {
    
              var ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
          
              ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
          
              ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
          
              ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
          
              return ret
          
            },
          
            transformlng(lng, lat) {
          
              var ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
          
              ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
          
              ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
          
              ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(lng / 30.0 * PI)) * 2.0 / 3.0;
          
              return ret
          
            },
            out_of_china(lng, lat) {
    
                return (lng < 72.004 || lng > 137.8347) || ((lat < 0.8293 || lat > 55.8271) || false);
            
              }
});