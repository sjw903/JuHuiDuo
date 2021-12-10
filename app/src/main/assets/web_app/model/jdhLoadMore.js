Vue.component("jdh-loadmore", {
	data: function() {
		return {
			refreshing:false,
			loading:false,
			loading_all:false,
			// 文章列表
			type_id_s: 0, // 当前分类ID
			news_list: [],
			page: 0,
			pagesize: 10,
			adv_cfg:[],
			vCount:0,
			tmp_id:0,
		}
	},
	template: `<mu-container ref="container" class="demo-loadmore-content"><mu-load-more @load="load" @refresh="refresh" :refreshing="refreshing" :loading="loading"><slot></slot>
	<div style="padding-bottom: 3rem;">
		<template v-for="item,name,index in news_list">
			<div class="ads_banner" v-if="isAddAds(name+2,name)">
				<jdh_adv idt="article_list" :cfg="adv_cfgs" :ref="'jdh_adv_tt'+name" :adv_index="name" class="for_bottom"></jdh_adv>
			</div>
			<!-- 第一种样式 - 上边文档标题，下边一张大图 -->
			<mu-row style="margin: .6rem auto .5rem auto; " @click="openDesc(item)"
				v-show="item.image_num < 3 && item.has_video === 0 " gutter>
				<mu-col :fill="true" span="12">
					<div class="news_title_three">{{item.title}}</div>
					<div class="news_one_contents" style="display: none;">
						这里没有图片{{item.image_list}}
					</div>
					<div class="news_sources">{{item.source}}</div>
				</mu-col>
			</mu-row>
			<!-- 第二种样式 - 第边文章标题 - 下边三张图 -->
			<mu-row style="margin: .6rem auto .5rem auto;" @click="openDesc(item)"
				v-show="item.image_num >= 3 && item.has_video === 0 " gutter>
				<mu-col :fill="true" span="12">
					<div class="news_title_three">{{item.title}}</div>
					<div class="news_three">
						<div class="news_three_img_item" v-for="img in item.image_list"><img
								:src=img.url></div>
					</div>
					<div class="news_sources">{{item.source}}</div>
				</mu-col>
			</mu-row>
			<!-- 第三种样式 - 左边文字，右边一张图 -->
			<mu-row style="margin: .6rem auto .5rem auto; display: none;" @click="openDesc(item)"
				v-show=" item.has_video === 1 " gutter>
				<mu-col :fill="true" span="12" style="display:none">
					<div class="video_item_poster_img">
						{{item.item_url}}
						<video id=example-video width=300 height=150 class="video-js vjs-default-skin"
							controls>
							<source :src="item.item_url" type="application/x-mpegURL">
						</video>
					</div>
					<div class="video_author">{{item.title | cut_title(25)}} </div>
				</mu-col>
			</mu-row>
			<!-- 第四种样式 - 仅有文字 -->
			<mu-row style="margin: .6rem auto .5rem auto; display:none" @click="openDesc(item)"
				 gutter>
				<mu-col :fill="true" span="12">
					<div class="grid-cell">{{item.title}}</div>
				</mu-col>
			</mu-row>
			<mu-divider v-show=" item.has_video !== 1 "></mu-divider>
		</template>
	</div>
	<div class="common_loaded_all" v-if="loading_all">已加载全部信息</div>
	</mu-load-more></mu-container>`,
	props: {
		type_id:{
			type:Number,
			default:0,
		},
		load_id:{
			type:Number,
			default:-1,
		},
		adv_cfgs:[],
		
	},
	watch:{
		load_id(){
			// 这里是变更
			if (this.load_id == this.type_id){
				for(var itm in this.adv_cfgs.place_list){
					if (this.adv_cfgs.place_list[itm].identify === "article_list"){
						
						this.adv_cfg = this.adv_cfgs.place_list[itm];
						break;
					}
				}
				if (this.news_list.length>0){
					this.refresh();
				}
				else{
					this.load();
				}
			}
		}
	},
	computed:{},
	mounted() {
		// if (this.load_id == this.type_id){
		// 	console.log("this_mounted")
		// 	console.log(this.load_id+",,,,,"+this.type_id)
		// 	if (this.news_list.length>0){
		// 		this.refresh();
		// 	}
		// 	else{
		// 		this.load();
		// 	}
		// }
		
		
		if (this.load_id == this.type_id){
			for(var itm in this.adv_cfgs.place_list){
				if (this.adv_cfgs.place_list[itm].identify === "article_list"){
					this.adv_cfg = this.adv_cfgs.place_list[itm];
					break;
				}
			}

			this.load();
		}
	},
	methods: {
		isAddAds(index,old){
			var tmp = index % this.adv_cfg.interval_num;
			if (tmp +1 == this.adv_cfg.interval_num && old != 0 ){
				return true;
			}
			else{
				return false;
			}
			
		},
		onload(){
			console.log("onload")
		},
		async load(){
			if (this.refreshing) return
			this.loading = true;
			this.page = this.page + 1;
			let that = this;
			console.log("load method")
			// console.log("this.adv_cfg",this.adv_cfg)
			await this.getXhmediaLiblist();
		},
		async refresh(){
			if (this.loading) return;
			this.news_list = []
			this.refreshing = true
			this.page = 1
			localReadLib.removeAllAdv();
			await this.getXhmediaLiblist()
			console.log("refresh method" + this.type_id)
		},
		async getXhmediaLiblist() {
			var that = this
			var data = {
				token: getToken(),
				type_id: this.type_id,
				page: this.page,
				per: this.pagesize,
			}
		
			console.log("进到 getXhmediaLiblist")
			await axios.post(api_list.getXhmediaLiblist, data).then(res => {
				// console.log(JSON.stringify(res))
				if (res.data.code === 0) {
					var tmp_json = res.data.list.data;
					var tmp = [];
					
					
					if (res.data.list.data.length > 0) {
						for (var i = 0; i < res.data.list.data.length; i++ ){
							if (res.data.list.data[i].has_video !== 0) continue;
							tmp.push(res.data.list.data[i]);
								
								// try {
								// 	// console.log(tmp_json[i].image_list);
								// 	tmp_json[i].image_list = JSON.parse(tmp_json[i].image_list)
								// } catch (ee) {
								// 	//console.log(ee);
								// }
							
						}
						
						for (var i = 0; i < tmp.length; i++) {
							try {
								
								// console.log(tmp_json[i].image_list);
								tmp[i].image_list = JSON.parse(tmp[i].image_list)
							} catch (ee) {
								//console.log(ee);
							}
						}
					}
					if (that.news_list.length === 0) {
						that.news_list = tmp
					} else {
						that.news_list = that.news_list.concat(tmp)
					}
					if (res.data.list.total < that.page*that.pagesize) that.loading_all = true;
					// console.log(JSON.stringify(res.data.request_data))
					if (res.data.request_data !== undefined && res.data.request_data.length != 0){
						if (typeof(juduohui)!= "undefined") juduohui.setProxy(JSON.stringify(res.data.request_data))
					}
					console.log("that.type_id = " + that.type_id )
					// that.$emit("loaded",that.type_id)
				}
				

				// console.log(that.news_list)
			}).catch(err => {
				console.log(err)
			})
			

			this.loading = false;
			this.refreshing = false;
			
		},
		async openDesc(item) {
			if (typeof(item) == "object") {
				localStorage.setItem("read_id", item.id);
				localStorage.setItem("item_id",item.item_id);
				localStorage.setItem("click_timestamp", new Date().getTime());
				// location.href = "news_desc.html";
				openNewActivite("news_desc.html")
			} else {
				alertMessage("非法操作", 0)
			}
		},
	}

});
