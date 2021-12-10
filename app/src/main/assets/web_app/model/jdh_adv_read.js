Vue.component("jdh_adv", {
	data: function() {
		return {
			name:"jdh_adv",
			channel:0,
			position:0,
			adv_clss:"",
			adv_display:"",
			adv_cfg:null, /* { identify,interval_num,list:[
				advertisement_id:"2022829840404425"
				am_id				:				29
				ap_id				:				18
				channel				:				(...)
				get_advertisement_count				:				(...)
				get_place_count				:				(...)
				hpoint				:				(...)
				max_watch_num				:				(...)
				max_watch_video				:				(...)
				place_type				:				(...)
				watch_interval_time				:				(...)
			] }*/
			adv_clss:"",
		}
	},
	template: `<div :class="'jdh_adv ' + adv_clss" ref="jdh_adv" style='display:'></div>`,
	props: {
		// 广告配置列表
		cfg:{
			default:null,
		},
		
		// 广告标识
		idt:{
			type:String,
			default:"",
		},
		adv_index:{
			default:-1
		}
	},
	watch:{
		cfg(){this.loadAdvs();},
		idt(){},
	},
	mounted() {
		// console.log("广告组件 mounted "+JSON.stringify(this.cfg))
		if (this.cfg!=null) this.loadAdvs()
		window.addEventListener("scroll",this.bodyScroll,true)
	},
	methods: {
		bodyScroll(event){
			// console.log(event)
			// console.log($("."+this.adv_clss).offset(),$("."+this.adv_clss).position())
			if (typeof(juduohui) !== "undefined") juduohui.setScrollTop();
		},
		getClss(){
			return this.adv_clss;
		},
		loadAdvs(){
			
			if (this.cfg == null) return;
			if (!getAdIsOpen()){
				this.$refs.jdh_adv.style.display = "none"
				return;
			}
			
			console.log("这里这里这里")
	
			this.adv_clss = this.idt + "_" + localReadLib.rndString();
			
			// console.log("广告组件" + getAdIsOpen() + "," + this.idt + ","+this.adv_clss)
			for (var itm in this.cfg.place_list){
				if (this.cfg.place_list[itm].identify === this.idt){
					this.adv_cfg = this.cfg.place_list[itm]
					break;
				}
			}
			
			if (this.adv_cfg !== null && this.adv_cfg.hasOwnProperty("list")){
				this.$refs.jdh_adv.style.display = ""
			}
			else{
				this.$refs.jdh_adv.style.display = "none"
			}
			
			
			if (this.adv_cfg == undefined || this.adv_cfg.list.length == 0) {
				console.log("广告配置为空")
				return;
			}
			
			var ad_channel_cfg = [];
			
				
			var last_feed_ad_index = localReadLib.load("last_feed_ad_index");
			// console.log(last_feed_ad_index);
			if (last_feed_ad_index == null) {
				last_feed_ad_index = localReadLib.rndInt(0,this.adv_cfg.list.length);
			}
			else{
				last_feed_ad_index = parseInt(last_feed_ad_index);
				if (last_feed_ad_index+1 >= this.adv_cfg.list.length){
					last_feed_ad_index = 0;
				}
				else{
					last_feed_ad_index = last_feed_ad_index+1
				}
				
			}
			localReadLib.save("last_feed_ad_index",last_feed_ad_index);
			// console.log(this.adv_cfg.list.length +"," + last_feed_ad_index)
			ad_channel_cfg = this.adv_cfg.list[last_feed_ad_index]//[localReadLib.rndInt(0,this.adv_cfg.list.length)];
			// console.log(JSON.stringify(ad_channel_cfg))
			
			this.channel = ad_channel_cfg['channel'];
			this.position = ad_channel_cfg['advertisement_id'];
			this.clss = this.adv_clss;
			// console.log("取广告配置："+ad_channel_cfg['channel']+","+ad_channel_cfg['advertisement_id']+","+this.adv_clss)
			if (ad_channel_cfg['channel'] === undefined) return;
			this.adv_display = "取广告配置："+ad_channel_cfg['channel']+","+ad_channel_cfg['advertisement_id']+","+this.adv_clss
			
			// console.log(ad_channel_cfg)
			var ad_type = 0;
			// | 3 | ad_type | 广告类型标识 banner：banner，excitation：激励视频，information：信息流，insert_screen：插屏广告 |
			switch(this.adv_cfg['ad_type']){
				case "excitation":
					ad_type = 1;
					break;
				case "banner":
					ad_type = 2;
					break;
				case "information":
					ad_type = 0;
					break;
				case "insert_screen":
					ad_type = 3;
					break;
			}
			
			localReadLib.saveJSON("tmp_" + this.clss + "_setting",this.adv_cfg.list,120);
			localReadLib.saveJSON("tmp_" + this.clss + "_index",last_feed_ad_index,120);
			
			localReadLib.renderAdvert({
				"type": ad_type,
				"channel":ad_channel_cfg['channel'],
				"position_id":ad_channel_cfg['advertisement_id'],
				"ad_type": ad_channel_cfg['ad_type'],
				"ads_class": "."+this.adv_clss,
				"order_id":"#order_id",
				"org_config":this.adv_cfg,
				"ondisplay":function(){},
				"onerror":function(advclss){
						console.log("重新加载广告位")
						var type_id = localReadLib.load("news_read_type_id") // 对应9
						var ad_index = localReadLib.load("tmp_" + advclss.replace(".") + "_index")
						this.$refs['jdh_load_'+  type_id][0].$refs.container.__vue__.$refs['jdh_adv_tt'+ad_index][0].loadAdvs()
						console.log("重新加载广告位")
				},
				"onclick":function(){},
				"ondownload":function(){},
				"onclose":function(){
					console.log(this.adv_clss)
					console.log(JSON.stringify(this.$refs))
					this.$refs.jdh_adv.style.display = "none"
				},
			});
			
			let that = this;
			setTimeout(()=>{ that.$emit("loaded",{"clazz":that.clss,"width":that.$refs.jdh_adv.offsetWidth,"height":that.$refs.jdh_adv.offsetHeight}) },500)
		}
	}

});
