<template>
    <div id="error-modal-bg" @click="errorClose"></div>
	<div id="error-modal-wrap">
		<div id="error-popup">
			<div id="error-popup-content">
				<div id="error-sentence"></div>
				<div id="error-btn-wrapper">
					<div id="btn">
						<el-button type="info" size="large" @click="errorClose">확인</el-button>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import { useStore } from 'vuex';

export default {
    name: "ErrorPopUp",

    data() {
        return {
            store: useStore(),
        };
    },
    computed: {
        status() {
            return this.store.state.errorModule.status;
        },
    },
    watch: {
        status(value){
            if(value == 200){
                return;
            }
            let sentenceTag = document.getElementById("error-sentence");
            switch(value){
				case 204:
                    sentenceTag.innerHTML = "평가가 반영되었습니다.";
                    break;
				case 205:
                    sentenceTag.innerHTML = "친구에게 초대를 보냈습니다.";
                    break;
                case 401:
                    sentenceTag.innerHTML = "비밀번호가 틀렸습니다.";
                    break;
				case 402:
                    sentenceTag.innerHTML = "이미 다른 방에 참여중입니다.";
                    break;
				case 403:
                    sentenceTag.innerHTML = "해당 친구는 참여중인 방이 없습니다.";
                    break;
                case 404:
                    sentenceTag.innerHTML = "해당 방을 찾을 수 없습니다.";
                    break;
				case 405:
                    sentenceTag.innerHTML = "강퇴당했습니다.";
                    break;
                case 409:
                    sentenceTag.innerHTML = "방 인원이 가득 찼습니다.";
                    break;
            }
            document.getElementById("error-modal-wrap").style.display = 'block';
            document.getElementById("error-modal-bg").style.display = 'block';
        },
    },
    methods: {
        errorClose() {
			this.store.commit("errorModule/SET_STATUS", 200);
            document.getElementById("error-modal-wrap").style.display = 'none';
            document.getElementById("error-modal-bg").style.display = 'none';
        }
    }
};
</script>

<style>
#error-modal-bg {
	display: none;
	width: 100%;
	height: 100%;
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	z-index: 999;
	transition: 0.5s ease-out;
}

#error-modal-wrap {
	display: none;
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	width: 30%;
	height: 30%;
	background: #252836;
	border: solid 2px #e27b66;
	border-radius: 2rem;
	z-index: 1000;
}

#error-popup {
	display: flex;
	align-items: center;
	height: 100%;
	width: 100%;
	/* grid-template-rows: 1fr 11fr; */
	transition: 0.5s ease-out;
	color: white;
}

/* .error-popup-header {
	background-color: black;
	height: 100%;
	width: 100%;
	border-bottom: solid 0.5rem #6E0000;
	border-radius: 1rem 1rem 0 0;
} */

#error-popup-content {
	display: flex;
	flex-direction: column;
	justify-content: space-around;
	align-items: center;
	height: 70%;
	width: 80%;
	margin: 10%;
}

#error-btn-wrapper {
	display: flex;
	justify-content: space-around;
}

/* 
.error-popup-header-title {
	color: white;
	font-size: 1.3rem;
	padding: 0 5%;
	padding-top: 1%;
} */

.el-button {
	background-color: #e27b66 !important;
	color: black !important;
	border: none;
}

.el-button:hover {
	opacity: 0.75;
	cursor: pointer;
}
</style>