var app = getApp()

Page({

  data: {
    hasmarkers:false,
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
  {     wx.setStorage({
    key: 'arrive',
    data: event.currentTarget.dataset,
  })
  wx.navigateTo({
    url: '../../searchindex/searchindex',
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
          width: 50,
          height: 50,
          name:'',
          bikeCount:''}
            marker.latitude=res.data[x].location.coordinates[1]
            marker.longitude=res.data[x].location.coordinates[0] 
            marker.name="noname"
            marker.iconPath = "../../../images/logo.png"
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
  unBind: function (e) {
    var that = this;
    console.log(e.currentTarget.dataset.name)
    //获取列表中要删除项的下标
    wx.showModal({
      title: '提示',
      content: '是否确认解绑',
      success(res) {
        if (res.confirm) {
          //解绑
          wx.request({
            url: app.globalData.root + "car/unBind.do",
            data: {
              "openid": app.globalData.openid,
              "carNum": e.currentTarget.dataset.name
            },
            method: 'GET',
            header: {
              'Content-type': 'application/json'
            },
            success: function (res) {
              that.getCars(app.globalData.openid);
            }
          });
        }
      }
    })

  },
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
});