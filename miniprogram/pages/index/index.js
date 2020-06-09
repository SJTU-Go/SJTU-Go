//index.js
const app = getApp()
var x_PI = 3.14159265358979324 * 3000.0 / 180.0;

var PI = 3.1415926535897932384626;

var a = 6378245.0;

var ee = 0.00669342162296594323;

Page({

  data:{
    step :0,
    bus:{},
    walk:{},
    pass:'',
    depart:'',
    arrive:'',
    index:['walk','bus'],
    value : new Array(),
    method:["步行","校园巴士"],
    preference:["步行","校园巴士"],
    preference1:['步行','共享单车','校园巴士'],
    preferencelist:[],
    banned:[],
    preferencelist: new Array(),
    searchtxt:'',
    datares: new Array(),
    markers: [
    {
      iconPath: "/mark/7.PNG",
      id: 0,
      latitude: 31.020502,//31.029236,
      longitude: 121.434009,//121.452591,
      width: 50,
      height: 50,
    },


    ],
        latitude:'31.021807',
    longitude:'121.429846',
    currentdata:new Array(),
    hasmarkers:false


  }  ,
    onLoad:function(){
     /* wx.request({
        url: 'https://api.ltzhou.com/user/preference',
        method:"POST",
        data:
          {
          "banlist": [],
          "preferencelist": [
            "步行",
            "共享单车",
            "校园巴士"
          ],
          "userID": 123
      },
      header: {
        'content-type': "application/x-www-form-urlencoded"
        },
        success(res){console.log(updatinggggggggooood)}
      })*/

  
    var that = this

    that.setData({ 
         pass:'',
    depart:'',
    arrive:'',})

    wx.request({
      url: 'https://api.ltzhou.com/map/nearby/parking',
      data:{"lat":"31.021807" ,"lng":"121.429846"},
      success(res){
        var x

        var markers=new Array();
        var q = 0
        for (x in res.data)
        {
          if (res.data[x].bikeCount)
          {         var marker ={iconPath: "/mark/19.PNG",
          id: q,
          latitude: 31.021807,//31.029236,
          longitude: 121.429846,//121.452591,
          width: 50,
          height: 50,
          name:'',
          bikeCount:''}
            marker.latitude=res.data[x].vertexInfo.location.coordinates[1]
            marker.longitude=res.data[x].vertexInfo.location.coordinates[0] 
            marker.name=res.data[x].vertexInfo.vertexName 
            marker.bikeCount=res.data[x].bikeCount
            marker.iconPath = "/mark/"+res.data[x].bikeCount+".png"
            console.log(marker)
            markers.push(marker) 
            console.log("adding")
            console.log(markers)
           q =q +1}}
           markers.push({iconPath: "../../images/crosses.png",
           id: q,
           latitude: that.data.latitude,
           longitude:that.data.longitude,
           width: 50,
           height: 50,
           name:'',
           bikeCount:''})      
         console.log(markers)
        that.setData({markers:markers})
      that.setData({hasmarkers:true})}
    })
  
  
  
  
    var that =this;
    wx.getStorage({
    key: 'preference',
    success: function(res){
    that.setData({
      preferencelist:res.data,
    })
    if(that.data. preferencelist.length==0){
    that.setData({
    status:false
    });
    }else{
    that.setData({
    status:true
    })
    }
    },
    fail: function(res) {
    console.log(res+'aaaaa')
    }
    });
    wx.getStorage({
      key: 'banned',
      success: function(res){
        console.log(res.data)
      that.setData({
        banned:res.data,

      })
      if(that.data.banned.length==0){
      that.setData({
      status:false
      });
      }else{
      that.setData({
      status:true
      })
      }

      if(that.data.preferencelist.length==0&&that.data.banned.length==0){
        that.setData({preferencelist:that.data.preference1})
        console.log(that.data.preferencelist)
        wx.setStorage({ key:'preference',
        data:that.data.preferencelist})
      }

      },
      fail: function(res) {
      console.log(res+'aaaaa')
      }
      });
      console.log(that.data.preferencelist)
      console.log(that.data.banned)
      console.log(that.data.preferencelist.length==0&&that.data.banned.length==0)

  
  
  },
    pass:function(e){
      this.setData({
        pass: e.detail.value,
      })
    },
    depart:function(e){
      this.setData({
        depart: e.detail.value,
      })
    },
    regionchange:function(e){
      var llatitude
      var llongitude
      console.log(e)
      if ((e.type == 'end' || e.type == 'begin')&& (e.causedBy == 'scale' || e.causedBy == 'drag'||e.causedBy=='gesture')) {
    
              var that = this;
        
              this.mapCtx = wx.createMapContext("map4select");
        
              this.mapCtx.getCenterLocation({
        
                type: 'gcj02',
        
                success: function(res) {
        console.log('location')
        console.log(that.data)
                  console.log(res, 11111)
        
                  var coordinate = that.gcj02towgs84(res.longitude, res.latitude)
        
                  console.log(coordinate, 2222)
        console.log(res.latitude)
        llatitude = res.latitude
        llongitude = res.longitude
        var mar = that.data.markers[that.data.markers.length-1]
        mar.latitude = llatitude
        mar.longitude = llongitude
        var h = that.data.markers
        h[that.data.markers.length-1]=mar
        console.log(h[that.data.markers.length-1])
        that.setData({markers:h})
        console.log("poping")


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
        
              })
            }


          },
      
    arrive:function(e){
      this.setData({
        arrive: e.detail.value,
      })
    },
    indexback: function(e)
    {this.setData({step:0})
    },
    formSubmit: function (e) {
      var depart=String( this.data.depart)
      var arrive=String( this.data.arrive)
      var pass=this.data.pass
      var passlist=[];
      if(pass){
      passlist.push(pass)};
      var that =this;
      var tem;
      var valuetem=new Array();
      var pre = new Array();
      var i;
      var j = 0;
      var preres = new Array();
      console.log({
        "arrivePlace": arrive,
        "beginPlace": depart,
        "departTime": "2020/05/11 12:05:12",
        "passPlaces": passlist,})
      wx.request({
        url: 'https://api.ltzhou.com/navigate/bus',
        method:'POST',
        header: {
        'content-type': 'application/json'
        },
        data:{
        "arrivePlace": arrive,
        "beginPlace": depart,
        "departTime": "2020/05/11 12:05:12",
        "passPlaces": passlist,},

        success (res) {
          tem = res.data
          console.log(tem)
          that.setData({bus:tem})
          valuetem.push(tem)
          wx.request({
            url: 'https://api.ltzhou.com/navigate/walk',
            method:'POST',
            header: {
            'content-type': 'application/json'},
          data:{
            "arrivePlace": arrive,
            "beginPlace": depart,
            "passPlaces": passlist,
            },
            success (res) {
              tem = res.data
              console.log(tem)
              valuetem.push(tem)
              that.setData({value:valuetem})
              console.log(that.data.value)
              console.log("1")
              for(j=0;j<that.data.preference.length;j++){
                for (i=0;i<that.data.value.length;i++){
                  if(that.data.value[i].type==that.data.preference[j]){pre.push(i)}}}
              for (i=0;i<pre.length;i++){preres.push(that.data.value[pre[i]])}
              console.log(preres)
              var ressss = new Array()
              ressss.push(preres)
              ressss.push(valuetem)
              ressss.push(that.data.bus.routeplan)
              ressss.push(depart)
              ressss.push(pass)
              ressss.push(arrive) 
              that.setData({datares:ressss})
              console.log(that.data.datares)

              wx.navigateTo({
                url: '../searcha/searcha?RT='+JSON.stringify(that.data.datares),
                //success:function(res){that.setData({step:0})}
              
              },

                )

            }}
              )             
    }})

  
  },
  navigatePage:function()
  { wx.setStorage({
    data:'',
    key: 'arrive',
  })  
  wx.setStorage({
    data:'',
    key: 'pass',
  })  
  wx.setStorage({
    data:'',
    key: 'depart',
  })  
    wx.navigateTo({
    url: '../searchindex/searchindex',
  })
  },

  setnavigatePage:function()
  { wx.setStorage({
    data:{name:this.data.markers[this.data.currentdata].name,
        
    },
    key: 'arrive',
  })  
  wx.setStorage({
    data:'',
    key: 'pass',
  })  
  wx.setStorage({
    data:'',
    key: 'depart',
  })  
    wx.navigateTo({
    url: '../searchindex/searchindex',
  })
  },

  searchInput:function(e)
  {    app.globalData.search =e.detail.value
    
  },
  search:function()
  {    
  },


  showModal: function (e) {
    console.log(e.markerId)
    this.setData({currentdata:e.markerId})
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationData: animation.export(),
      showModalStatus: true
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationData: animation.export()
      })
    }.bind(this), 200)
  },

  //隐藏弹框
  hideModal: function () {
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationData: animation.export(),
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationData: animation.export(),
        showModalStatus: false
      })
    }.bind(this), 200)
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



})

/*

            
            
            
              data: {
     latitude: 31.020502,//31.029236,
      longitude: 121.434009,//121.452591,


                         markers.push({iconPath: "../../images/crosses.png",
           id: q,
           latitude: that.data.latitude,
           longitude:that.data.longitude,
           width: 50,
           height: 50,
           name:'',
           bikeCount:''})    
            
            */