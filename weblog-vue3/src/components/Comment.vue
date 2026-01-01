<template>
    <div class="mt-14">
        <h2 class="flex justify-center items-center mb-7 text-gray-500 text-sm font-medium">全部评论<span>({{ total }})</span></h2>
        
        <div :class="props.customeCss">
            <form>
                <div class="flex gap-4 items-center">
                    <div class="flex-shrink-0">
                        <img v-if="loginUserInfo.avatar" :src="loginUserInfo.avatar" 
                             class="w-10 h-10 rounded-full object-cover border border-gray-100 shadow-sm">
                        <svg v-else class="w-10 h-10 text-gray-300" fill="currentColor" viewBox="0 0 20 20">
                            <path d="M10 0a10 10 0 1 0 10 10A10.011 10.011 0 0 0 10 0Zm0 5a3 3 0 1 1 0 6 3 3 0 0 1 0-6Zm0 13a8.949 8.949 0 0 1-4.951-1.488A3.987 3.987 0 0 1 9 13h2a3.987 3.987 0 0 1 3.951 3.512A8.949 8.949 0 0 1 10 18Z" />
                        </svg>
                    </div>
                    <div class="grow">
                        <div class="w-full border border-gray-200 rounded-full bg-gray-50 dark:bg-gray-700 dark:border-gray-600 focus-within:border-sky-500 focus-within:bg-white transition-all overflow-hidden flex items-center pr-2">
                            <textarea rows="1" v-model="commentForm.content"
                                class="grow px-5 py-2.5 text-sm text-gray-900 bg-transparent border-0 focus:ring-0 dark:text-white dark:placeholder-gray-400 resize-none"
                                placeholder="发个友善的评论吧..." required></textarea>
                            
                            <div class="flex items-center space-x-1">
                                <div data-popover-target="popover-emoji" class="p-1.5 text-gray-400 rounded-full cursor-pointer hover:bg-gray-100 dark:hover:bg-gray-600">
                                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 9h0M9 9h0m12 3a9 9 0 1 1-18 0 9 9 0 0 1 18 0ZM7 13c0 1 .5 2.4 1.5 3.2a5.5 5.5 0 0 0 7 0c1-.8 1.5-2.2 1.5-3.2 0 0-2 1-5 1s-5-1-5-1Z" />
                                    </svg>
                                </div>
                                <div data-popover id="popover-emoji" role="tooltip" class="absolute z-10 invisible w-64 text-sm bg-white border border-gray-200 rounded-lg shadow-sm opacity-0 dark:bg-gray-800 dark:border-gray-600">
                                    <div class="p-2 grid grid-cols-6 gap-2">
                                        <div v-for="emoji in emojis" :key="emoji" class="text-xl hover:scale-125 cursor-pointer transition-transform text-center" @click="addEmoji(emoji)">{{ emoji }}</div>
                                    </div>
                                </div>
                                <button type="button" @click="onPublishCommentClick" 
                                    class="p-2 text-sky-600 hover:text-sky-700 transition-colors">
                                    <svg class="w-6 h-6" fill="currentColor" viewBox="0 0 20 20"><path d="M10.894 2.553a1 1 0 00-1.788 0l-7 14a1 1 0 001.169 1.409l5-1.429A1 1 0 009 15.571V11a1 1 0 112 0v4.571a1 1 0 00.725.962l5 1.428a1 1 0 001.17-1.408l-7-14z"/></svg>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </form>

            <div v-if="comments && comments.length > 0" class="mt-10">
                <div v-for="(comment, index) in comments" :key="comment.id">
                    <div v-if="index > 0" class="border-t ml-14 mt-6 border-gray-50 dark:border-gray-700"></div>

                    <div class="flex gap-4 mt-6">
                        <img :src="comment.avatar || '/default-avatar.png'" class="w-10 h-10 rounded-full object-cover shadow-sm">
                        <div class="flex flex-col gap-1 grow">
                            <div class="text-xs text-[#FB7299] font-bold">{{ comment.nickname }}</div>
                            <div class="text-sm text-gray-700 dark:text-gray-300 leading-relaxed">{{ comment.content }}</div>
                            <div class="flex items-center text-[11px] text-gray-400 mt-2">
                                <div>{{ comment.createTime }}</div>
                                <div class="ml-4 cursor-pointer hover:text-sky-600"
                                    @click="showReplyForm(index, comment.nickname, comment.id, comment.id)">
                                    回复
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="ml-14 mt-2" v-if="comment.childComments && comment.childComments.length > 0">
                        <div v-for="childComment in (comment.isExpanded ? comment.childComments : comment.childComments.slice(0, 3))" :key="childComment.id" class="py-3">
                            <div class="flex items-start gap-3">
                                <img :src="childComment.avatar || '/default-avatar.png'" class="w-6 h-6 rounded-full object-cover">
                                <div class="flex flex-col grow gap-1.5">
                                    <div class="text-xs">
                                        <span class="font-bold text-gray-600 dark:text-gray-400 mr-1">{{ childComment.nickname }}</span>
                                        <span v-if="childComment.replyNickname" class="text-gray-400 font-normal">
                                            回复 <span class="text-sky-600 font-medium">@{{ childComment.replyNickname }}</span>
                                        </span>
                                    </div>
                                    <div class="text-sm text-gray-800 dark:text-gray-200 leading-relaxed">
                                        {{ childComment.content }}
                                    </div>
                                    <div class="flex items-center text-[10px] text-gray-400">
                                        <div>{{ childComment.createTime }}</div>
                                        <div class="ml-3 cursor-pointer hover:text-sky-600"
                                            @click="showReplyForm(index, childComment.nickname, childComment.id, comment.id)">
                                            回复
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div v-if="comment.childComments.length > 3" class="mt-1">
                            <button @click="comment.isExpanded = !comment.isExpanded" 
                                    class="text-[11px] text-gray-400 hover:text-sky-600 transition-colors ml-9 bg-gray-50 dark:bg-gray-700/50 px-2 py-1 rounded-md">
                                {{ comment.isExpanded ? '收起回复' : `展开剩余 ${comment.childComments.length - 3} 条回复...` }}
                            </button>
                        </div>
                    </div>

                    <form class="ml-14 mt-4 mb-2" v-if="comment.isShowReplyForm">
                        <div class="flex gap-4 items-center">
                            <div class="grow">
                                <div class="w-full border border-gray-200 rounded-full bg-gray-100 dark:bg-gray-900 dark:border-gray-700 focus-within:border-sky-500 focus-within:bg-white transition-all flex items-center pr-1 group shadow-inner">
                                    <input v-model="replyContent" 
                                           class="grow px-5 py-2 text-sm bg-transparent border-0 focus:ring-0 dark:text-white" 
                                           :placeholder="replyPlaceholderText" />
                                    <button type="button" @click="onReplyContentSubmit" 
                                            class="bg-sky-500 hover:bg-sky-600 text-white text-[12px] font-bold px-4 py-1.5 rounded-full transition-transform active:scale-95 shadow-sm mr-1">
                                        发送
                                    </button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            
            <div v-else class="flex items-center mt-10 mb-10 justify-center text-gray-400 text-sm italic">
                暂无评论，留下你的足迹吧~
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, computed, watch } from 'vue'
import { initTooltips, initPopovers } from 'flowbite'
import { publishComment, getComments } from '@/api/frontend/comment'
import { useRoute } from 'vue-router'
import { showMessage } from '@/composables/util'
import { useBlogSettingsStore } from '@/stores/blogsettings'

const route = useRoute()
const blogSettingsStore = useBlogSettingsStore()

const props = defineProps({
    customeCss: {
        type: String,
        default: 'w-full px-6 py-8 mb-3 bg-white border border-gray-100 rounded-2xl dark:bg-gray-800 dark:border-gray-700 shadow-sm'
    }
})

// 监听路由刷新
watch(() => route.path, (newPath) => {
    commentForm.routerUrl = newPath
    initComments()
})

const loginUserInfo = computed(() => {
    const userStr = localStorage.getItem('user')
    if (!userStr) return { id: null, nickname: '', avatar: '' }
    try {
        const userObj = JSON.parse(userStr)
        const info = userObj?.userInfo || {}
        return {
            id: info.userID || null,
            nickname: info.nickname || info.username || '匿名用户',
            avatar: blogSettingsStore.blogSettings.avatar || ''
        }
    } catch (e) { return { id: null, nickname: '', avatar: '' } }
})

const emojis = ref(['😃', '😁', '😅', '😂', '😍', '😜', '😝', '🤑', '🥵', '🥰', '😎', '😭', '🥳', '❤️', '👍', '👏', '🙏'])
const comments = ref([])
const total = ref(0)
const replyContent = ref('')
const replyPlaceholderText = ref('发个回复...')
const currReplyCommentId = ref(null) 
const currParentCommentId = ref(null) 

const commentForm = reactive({
    avatar: '',
    content: '',
    nickname: '',
    routerUrl: route.path,
    fromUserId: null,
    replyCommentId: null,
    parentCommentId: null
})

onMounted(() => {
    initComments()
    const userStr = localStorage.getItem('user')
    if (userStr) {
        const userObj = JSON.parse(userStr)
        const currentLoginUserId = userObj.userInfo?.userID 
        if (currentLoginUserId) blogSettingsStore.getBlogSettings(currentLoginUserId)
    }
    syncUserInfoToForm() 
})

function initComments() {
    getComments(route.path).then(res => {
        if (res.success) {
            total.value = res.data.total
          
            comments.value = res.data.comments.map(c => ({ 
                ...c, 
                isShowReplyForm: false,
                isExpanded: false 
            }))
            nextTick(() => {
                initTooltips()
                initPopovers()
            })
        }
    })
}

function syncUserInfoToForm() {
    const user = loginUserInfo.value
    if (user.id) {
        commentForm.fromUserId = user.id
        commentForm.nickname = user.nickname
        commentForm.avatar = user.avatar 
        commentForm.routerUrl = route.path
    }
}

const onPublishCommentClick = () => {
    syncUserInfoToForm() 
    if (!commentForm.fromUserId) return showMessage('请先登录', 'warning')
    if (!commentForm.content.trim()) return showMessage('内容不能为空', 'warning')
    publishComment(commentForm).then(res => {
        if (res.success) {
            showMessage('发布成功')
            commentForm.content = ''
            initComments()
        }
    })
}

const showReplyForm = (index, nickname, replyCommentId, parentCommentId) => {
    if (!loginUserInfo.value.id) return showMessage('请先登录', 'warning')
    // 关闭其他已打开的回复框
    comments.value.forEach((c, i) => { if(i !== index) c.isShowReplyForm = false })
    
    const comment = comments.value[index]
    comment.isShowReplyForm = !comment.isShowReplyForm
    currReplyCommentId.value = replyCommentId
    currParentCommentId.value = parentCommentId
    replyPlaceholderText.value = `回复 @${nickname}:`
    if (comment.isShowReplyForm) nextTick(() => { initPopovers(); initTooltips(); })
}

const onReplyContentSubmit = () => {
    syncUserInfoToForm() 
    if (!replyContent.value.trim()) return showMessage('内容不能为空', 'warning')
    const replyData = { ...commentForm, content: replyContent.value, replyCommentId: currReplyCommentId.value, parentCommentId: currParentCommentId.value, routerUrl: route.path }
    publishComment(replyData).then(res => {
        if (res.success) {
            showMessage('回复成功')
            replyContent.value = ''
            initComments()
        }
    })
}

const addEmoji = (emoji) => commentForm.content += emoji
</script>