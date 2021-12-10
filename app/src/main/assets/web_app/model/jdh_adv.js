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
		// 同一个位置的第几个广告
		idx:{
			default:0,
		},
		refm:{
			default:"jdh_adv"
		}
	},
	watch:{
		cfg(){
			console.log("广告配置")
			console.log(this.cfg )
			this.loadAdvs()
			},
		idt(){},
	},
	mounted() {
		console.log("广告")
		this.loadAdvs()
	},
	methods: {
		getClss(){
			return this.adv_clss;
		},
		loadAdvs(){
			console.log("this.cfg =" + this.cfg);
			console.log("this.ref =" + this.refm);
			
			if (this.cfg == null || this.cfg == [] || this.cfg == '') return;
			
			if (!getAdIsOpen()){
				this.$refs.jdh_adv.style.display = "none"
				return;
			}
			
			
			
			
			this.adv_clss = this.idt + "_" + localReadLib.rndString();
			
			console.log("广告组件" + getAdIsOpen() + "," + this.idt + ","+this.adv_clss)
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
			
			var ad_channel_cfg = [];
			
			if (this.adv_cfg == undefined || this.adv_cfg.list.length == 0) {
				console.log("广告配置为空")
				return;
			}
					
			// this.adv_cfg.list.sort(function(a,b){
			// 	return a.channel-b.channel;
			// })
			
			var last_index_ad_index =  localReadLib.load("last_index_ad_index");
			
			if (last_index_ad_index == null || this.idx == 1) {
				last_index_ad_index = localReadLib.rndInt(0,this.adv_cfg.list.length);
			}
			else{
				last_index_ad_index = parseInt(last_index_ad_index);
				last_index_ad_index = last_index_ad_index + 1 >= this.adv_cfg.list.length ? 0 : last_index_ad_index + 1;
			}
			
			localReadLib.save("last_index_ad_index",last_index_ad_index);
			
			ad_channel_cfg = this.adv_cfg.list[last_index_ad_index]
			
			
			console.log("last_index_ad_index."+ last_index_ad_index +",channel:"+ad_channel_cfg['channel'])
			//原 ad_channel_cfg = this.adv_cfg.list[localReadLib.rndInt(0,this.adv_cfg.list.length)];
			
			this.channel = ad_channel_cfg['channel'];
			this.position = ad_channel_cfg['advertisement_id'];
			this.clss = this.adv_clss;
			if (ad_channel_cfg['channel'] === undefined) return;
			this.adv_display = "取广告配置："+ad_channel_cfg['channel']+","+ad_channel_cfg['advertisement_id']+","+this.adv_clss
			
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
			var that = this;
			localReadLib.saveJSON("tmp_" + this.clss,this.adv_cfg.list,120);
				localReadLib.renderAdvert({
					"type": ad_type,
					"org_config":this.adv_cfg,
					"channel":ad_channel_cfg['channel'],
					"position_id":ad_channel_cfg['advertisement_id'],
					"ad_type": ad_channel_cfg['ad_type'],
					"ads_class": "."+that.adv_clss,
					"order_id":"#order_id",
					"ondisplay":function(){
						console.log('显示时调用')
					},
					"onerror":function(adv_info){
						console.log("jdh_on_error：" +JSON.stringify(adv_info));
						console.log("重新加载广告位")
						eval("this.$refs."+this.refm).loadAdvs();
						
						console.log("重新加载广告位")
					},
					"onclick":function(){
						//点击广告后有的广告可能会消失，重新加载一下广告
						this.refreshAdvertising()
					},
					"ondownload":function(){},
					"onclose":function(){
						console.log('广告关闭时调用')
						this.closeBase();
						// that.$refs.jdh_adv.style.display = "none"
					},
				});
			
			that.$emit("loaded",{"clazz":that.clss,"width":that.$refs.jdh_adv.offsetWidth,"height":that.$refs.jdh_adv.offsetHeight})
		}
	}

});
