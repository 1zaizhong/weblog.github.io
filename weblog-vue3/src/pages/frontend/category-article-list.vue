<template>
    <Header></Header>

    <!-- 主内容区域 -->
    <main class="container max-w-screen-xl mx-auto px-4 md:px-6 py-4">
        <!-- grid 表格布局，分为 4 列 -->
        <div class="grid grid-cols-4 gap-7">
            <!-- 左边栏，占用 3 列 -->
            <div class="col-span-4 md:col-span-3 mb-3">
                <!-- 分类列表 -->
                <div class="w-full p-5 pb-7 mb-3 bg-white border border-gray-200 rounded-lg dark:bg-gray-800 dark:border-gray-700">
                    <!-- 分类标题 -->
                    <h2 class="flex items-center mb-5 font-bold text-gray-900 uppercase dark:text-white">
                        <!-- 文件夹图标 -->
                        <svg t="1698998570037" class="inline icon w-5 h-5 mr-2" viewBox="0 0 1024 1024" version="1.1"
                            xmlns="http://www.w3.org/2000/svg" p-id="21572" width="200" height="200">
                            <path
                                d="M938.666667 464.592593h-853.333334v-265.481482c0-62.577778 51.2-113.777778 113.777778-113.777778h128.948148c15.17037 0 28.444444 3.792593 41.718519 11.377778l98.607407 64.474074h356.503704c62.577778 0 113.777778 51.2 113.777778 113.777778v189.62963z"
                                fill="#3A69DD" p-id="21573"></path>
                            <path
                                d="M805.925926 398.222222h-587.851852v-125.155555c0-24.651852 20.859259-45.511111 45.511111-45.511111h496.82963c24.651852 0 45.511111 20.859259 45.511111 45.511111V398.222222z"
                                fill="#D9E3FF" p-id="21574"></path>
                            <path
                                d="M843.851852 417.185185h-663.703704v-98.607407c0-28.444444 22.755556-53.096296 53.096296-53.096297h559.407408c28.444444 0 53.096296 22.755556 53.096296 53.096297V417.185185z"
                                fill="#FFFFFF" p-id="21575"></path>
                            <path
                                d="M786.962963 938.666667h-549.925926c-83.437037 0-151.703704-68.266667-151.703704-151.703704V341.333333s316.681481 37.925926 430.45926 37.925926c189.62963 0 422.874074-37.925926 422.874074-37.925926v445.62963c0 83.437037-68.266667 151.703704-151.703704 151.703704z"
                                fill="#5F7CF9" p-id="21576"></path>
                            <path
                                d="M559.407407 512h-75.851851c-20.859259 0-37.925926-17.066667-37.925926-37.925926s17.066667-37.925926 37.925926-37.925926h75.851851c20.859259 0 37.925926 17.066667 37.925926 37.925926s-17.066667 37.925926-37.925926 37.925926z"
                                fill="#F9D523" p-id="21577"></path>
                        </svg>
                        分类
                        <span v-if="categories && categories.length > 0"
                            class="ml-2 text-gray-600 font-normal dark:text-gray-300">
                            ( {{ categories.length }} )
                        </span>
                    </h2>
                    <!-- 分类列表 -->
                    <div class="text-sm flex flex-wrap gap-3 font-medium text-gray-600 rounded-lg dark:border-gray-600 dark:text-white">
                        <a @click="goCategoryArticleListPage(category.id, category.name)"
                            v-for="(category, index) in categories" :key="index"
                            :class="[route.query.name == category.name ? 'bg-sky-100 hover:bg-sky-200' : 'hover:bg-gray-100']"
                            class="inline-flex cursor-pointer items-center px-4 py-2 text-sm font-medium text-center border rounded-lg 
             focus:ring-4 focus:outline-none focus:ring-gray-300 
            dark:bg-gray-800 dark:text-gray-300 dark:hover:bg-gray-700 dark:focus:ring-gray-800 dark:border-gray-700 dark:hover:text-white">
                            {{ category.name }}
                            <span
                                class="inline-flex items-center justify-center w-4 h-4 ms-2 text-xs font-semibold text-sky-800 bg-sky-200 rounded-full">
                                {{ category.articlesTotal }}
                            </span>
                        </a>
                    </div>
                </div>

                <!-- 分类文章列表 -->
                <div class="p-5 mb-4 border border-gray-200 rounded-lg bg-white dark:bg-gray-800 dark:border-gray-700">
                    <ol v-if="articles && articles.length > 0" class="divide-y divider-gray-200 dark:divide-gray-700">
                        <li v-for="(article, index) in articles" :key="index">
                            <a @click="goArticleDetailPage(article.id)" class="cursor-pointer items-center block p-3 sm:flex hover:bg-gray-100 hover:rounded-lg dark:hover:bg-gray-700">
                                <img class="w-24 h-12 mb-3 mr-3 rounded-lg sm:mb-0" :src="article.cover" />
                                <div class="text-gray-600 dark:text-gray-400">
                                    <h2 class="text-base font-normal text-gray-900 dark:text-white">
                                        {{ article.title }}
                                    </h2>
                                    <span
                                        class="inline-flex items-center text-xs font-normal text-gray-500 dark:text-gray-400">
                                        <svg class="inline w-2.5 h-2.5 mr-2 text-gray-400"
                                            aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none"
                                            viewBox="0 0 20 20">
                                            <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
                                                stroke-width="2"
                                                d="M5 1v3m5-3v3m5-3v3M1 7h18M5 11h10M2 3h16a1 1 0 0 1 1 1v14a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1Z" />
                                        </svg>
                                        {{ article.createDate }}
                                    </span>
                                </div>
                            </a>
                        </li>
                    </ol>

                    <!-- 该分类下没有文章提示，指定为 flex 布局，内容垂直水平居中，并纵向排列  -->
                    <div v-else class="flex items-center justify-center flex-col">
                        <svg height="300" node-id="1" sillyvg="true" template-height="1024" template-width="1024" version="1.1" viewBox="0 0 1024 1024" width="1024" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"><defs node-id="27"></defs><path d="M 0.00 0.00 L 1024.00 0.00 L 1024.00 1024.00 L 0.00 1024.00 Z" fill="none" node-id="30" stroke="none" target-height="1024" target-width="1024" target-x="0" target-y="0"/><g node-id="67"><path d="M 459.00 505.00 L 567.00 505.00 L 568.00 887.00 L 459.00 887.00 L 459.00 505.00 Z M 792.00 701.00 C 795.00 743.00 792.00 796.00 793.00 840.00 C 793.00 867.00 798.00 885.00 770.00 886.00 L 630.00 886.00 C 611.00 887.00 611.00 890.00 611.00 872.00 C 610.00 833.00 608.00 729.00 611.00 701.00 L 792.00 701.00 Z M 235.00 705.00 C 236.00 699.00 234.00 702.00 242.00 701.00 C 243.00 700.00 251.00 700.00 253.00 701.00 C 261.00 701.00 269.00 701.00 277.00 701.00 C 311.00 700.00 393.00 698.00 416.00 701.00 L 417.00 887.00 L 253.00 887.00 C 244.00 885.00 250.00 887.00 244.00 883.00 C 239.00 885.00 240.00 886.00 240.00 882.00 C 235.00 882.00 236.00 884.00 237.00 879.00 C 232.00 880.00 234.00 880.00 236.00 875.00 C 232.00 873.00 234.00 881.00 233.00 868.00 L 234.00 865.00 C 229.00 857.00 231.00 759.00 231.00 744.00 C 231.00 735.00 228.00 710.00 235.00 705.00 Z M 793.00 591.00 C 796.00 606.00 794.00 624.00 794.00 640.00 C 794.00 665.00 791.00 660.00 767.00 660.00 L 610.00 659.00 L 609.00 602.00 C 610.00 587.00 610.00 595.00 613.00 591.00 L 793.00 591.00 Z M 234.00 594.00 C 241.00 586.00 313.00 589.00 323.00 589.00 C 339.00 589.00 354.00 589.00 369.00 589.00 L 404.00 590.00 C 422.00 592.00 408.00 590.00 415.00 594.00 L 417.00 653.00 C 413.00 664.00 409.00 661.00 396.00 661.00 L 253.00 661.00 C 226.00 662.00 235.00 660.00 234.00 655.00 C 230.00 651.00 231.00 617.00 231.00 607.00 C 231.00 590.00 232.00 595.00 234.00 594.00 Z M 612.00 505.00 L 791.00 506.00 L 790.00 550.00 L 611.00 549.00 L 612.00 505.00 Z M 233.00 505.00 L 415.00 506.00 L 415.00 549.00 L 232.00 550.00 L 233.00 505.00 Z M 568.00 462.00 C 565.00 463.00 574.00 462.00 561.00 464.00 L 500.00 464.00 C 452.00 463.00 457.00 467.00 457.00 439.00 C 458.00 413.00 455.00 326.00 461.00 310.00 L 568.00 308.00 L 571.00 445.00 C 571.00 447.00 571.00 452.00 571.00 454.00 C 570.00 460.00 568.00 462.00 568.00 462.00 Z M 211.00 456.00 C 205.00 437.00 209.00 389.00 209.00 367.00 C 209.00 328.00 200.00 311.00 242.00 311.00 C 299.00 311.00 359.00 310.00 417.00 311.00 L 415.00 462.00 C 381.00 463.00 343.00 462.00 309.00 462.00 C 280.00 462.00 230.00 469.00 211.00 456.00 Z M 610.00 312.00 C 649.00 309.00 780.00 306.00 813.00 313.00 C 825.00 320.00 821.00 374.00 821.00 396.00 C 821.00 449.00 829.00 462.00 796.00 462.00 L 611.00 461.00 L 610.00 312.00 Z M 484.00 267.00 C 441.00 268.00 393.00 254.00 362.00 239.00 C 313.00 215.00 303.00 179.00 334.00 152.00 C 400.00 94.00 474.00 206.00 484.00 267.00 Z M 546.00 268.00 C 545.00 236.00 582.00 186.00 598.00 169.00 C 621.00 146.00 658.00 116.00 696.00 151.00 C 762.00 210.00 627.00 269.00 546.00 268.00 Z M 190.00 619.00 C 191.00 671.00 189.00 726.00 189.00 779.00 C 188.00 830.00 179.00 890.00 213.00 915.00 C 243.00 939.00 313.00 929.00 358.00 929.00 C 410.00 929.00 462.00 929.00 514.00 929.00 C 568.00 929.00 621.00 929.00 674.00 929.00 C 725.00 930.00 788.00 938.00 816.00 912.00 C 847.00 883.00 835.00 824.00 835.00 773.00 C 835.00 723.00 833.00 666.00 836.00 616.00 C 853.00 623.00 885.00 652.00 891.00 666.00 C 881.00 680.00 868.00 691.00 874.00 721.00 C 878.00 739.00 893.00 752.00 909.00 758.00 C 972.00 782.00 1015.00 686.00 953.00 656.00 C 939.00 649.00 940.00 656.00 930.00 642.00 C 910.00 615.00 885.00 592.00 856.00 574.00 C 849.00 569.00 841.00 567.00 834.00 561.00 L 834.00 501.00 C 867.00 481.00 863.00 469.00 863.00 422.00 C 862.00 365.00 884.00 273.00 807.00 268.00 C 774.00 266.00 737.00 270.00 703.00 268.00 C 714.00 254.00 728.00 250.00 741.00 231.00 C 796.00 149.00 682.00 33.00 574.00 130.00 C 538.00 162.00 523.00 207.00 515.00 218.00 C 498.00 182.00 481.00 151.00 456.00 129.00 C 346.00 36.00 240.00 146.00 285.00 228.00 C 295.00 248.00 313.00 257.00 318.00 268.00 C 141.00 264.00 166.00 285.00 165.00 425.00 C 165.00 471.00 163.00 476.00 191.00 500.00 L 191.00 566.00 C 145.00 580.00 111.00 627.00 91.00 645.00 C 83.00 653.00 78.00 651.00 68.00 657.00 C 12.00 687.00 51.00 786.00 118.00 757.00 C 182.00 729.00 136.00 671.00 134.00 666.00 C 143.00 652.00 170.00 623.00 190.00 619.00 Z" fill="#010101" fill-rule="evenodd" group-id="1" node-id="33" stroke="none" target-height="906" target-width="1003" target-x="12" target-y="33"/><path d="M 459.00 887.00 L 568.00 887.00 L 567.00 505.00 L 459.00 505.00 Z" fill="#fce201" fill-rule="evenodd" group-id="1" node-id="35" stroke="none" target-height="382" target-width="109" target-x="459" target-y="505"/><path d="M 792.00 701.00 L 611.00 701.00 C 608.00 729.00 610.00 833.00 611.00 872.00 C 611.00 890.00 611.00 887.00 630.00 886.00 L 770.00 886.00 C 798.00 885.00 793.00 867.00 793.00 840.00 C 792.00 796.00 795.00 743.00 792.00 701.00 Z" fill="#06ce6d" fill-rule="evenodd" group-id="1" node-id="37" stroke="none" target-height="189" target-width="190" target-x="608" target-y="701"/><path d="M 611.00 461.00 L 796.00 462.00 C 829.00 462.00 821.00 449.00 821.00 396.00 C 821.00 374.00 825.00 320.00 813.00 313.00 C 780.00 306.00 649.00 309.00 610.00 312.00 L 611.00 461.00 Z" fill="#06ce6d" fill-rule="evenodd" group-id="1" node-id="39" stroke="none" target-height="156" target-width="219" target-x="610" target-y="306"/><path d="M 234.00 865.00 L 233.00 868.00 C 236.00 870.00 236.00 863.00 236.00 875.00 L 237.00 879.00 C 241.00 879.00 240.00 878.00 240.00 882.00 L 244.00 883.00 L 414.00 884.00 L 413.00 704.00 L 235.00 705.00 C 238.00 719.00 238.00 851.00 234.00 865.00 Z" fill="#06ce6d" fill-rule="evenodd" group-id="1" node-id="41" stroke="none" target-height="180" target-width="181" target-x="233" target-y="704"/><path d="M 211.00 456.00 C 230.00 469.00 280.00 462.00 309.00 462.00 C 343.00 462.00 381.00 463.00 415.00 462.00 L 417.00 311.00 C 359.00 310.00 299.00 311.00 242.00 311.00 C 200.00 311.00 209.00 328.00 209.00 367.00 C 209.00 389.00 205.00 437.00 211.00 456.00 Z" fill="#06ce6d" fill-rule="evenodd" group-id="1" node-id="43" stroke="none" target-height="159" target-width="217" target-x="200" target-y="310"/><path d="M 460.00 459.00 L 568.00 461.00 L 566.00 312.00 L 461.00 313.00 Z" fill="#fce201" fill-rule="evenodd" group-id="1" node-id="45" stroke="none" target-height="149" target-width="108" target-x="460" target-y="312"/><path d="M 484.00 267.00 C 474.00 206.00 400.00 94.00 334.00 152.00 C 303.00 179.00 313.00 215.00 362.00 239.00 C 393.00 254.00 441.00 268.00 484.00 267.00 Z" fill="#fce201" fill-rule="evenodd" group-id="1" node-id="47" stroke="none" target-height="174" target-width="181" target-x="303" target-y="94"/><path d="M 546.00 268.00 C 627.00 269.00 762.00 210.00 696.00 151.00 C 658.00 116.00 621.00 146.00 598.00 169.00 C 582.00 186.00 545.00 236.00 546.00 268.00 Z" fill="#fce201" fill-rule="evenodd" group-id="1" node-id="49" stroke="none" target-height="153" target-width="217" target-x="545" target-y="116"/><path d="M 234.00 655.00 C 252.00 661.00 304.00 657.00 326.00 657.00 C 351.00 657.00 399.00 662.00 417.00 653.00 L 415.00 594.00 C 355.00 594.00 295.00 593.00 234.00 594.00 L 234.00 655.00 Z" fill="#fce201" fill-rule="evenodd" group-id="1" node-id="51" stroke="none" target-height="69" target-width="183" target-x="234" target-y="593"/><path d="M 612.00 595.00 L 613.00 656.00 L 790.00 656.00 L 791.00 595.00 L 697.00 593.00 L 688.00 594.00 Z" fill="#fce201" fill-rule="evenodd" group-id="1" node-id="53" stroke="none" target-height="63" target-width="179" target-x="612" target-y="593"/><path d="M 232.00 550.00 L 415.00 549.00 L 415.00 506.00 L 233.00 505.00 Z" fill="#06ce6d" fill-rule="evenodd" group-id="1" node-id="55" stroke="none" target-height="45" target-width="183" target-x="232" target-y="505"/><path d="M 611.00 549.00 L 790.00 550.00 L 791.00 506.00 L 612.00 505.00 Z" fill="#06ce6d" fill-rule="evenodd" group-id="1" node-id="57" stroke="none" target-height="45" target-width="180" target-x="611" target-y="505"/><path d="M 234.00 865.00 C 238.00 851.00 238.00 719.00 235.00 705.00 L 413.00 704.00 L 414.00 884.00 L 244.00 883.00 L 240.00 882.00 C 240.00 878.00 241.00 879.00 237.00 879.00 L 236.00 875.00 C 236.00 863.00 236.00 870.00 233.00 868.00 C 234.00 881.00 232.00 873.00 236.00 875.00 C 234.00 880.00 232.00 880.00 237.00 879.00 C 236.00 884.00 235.00 882.00 240.00 882.00 C 240.00 886.00 239.00 885.00 244.00 883.00 C 250.00 887.00 244.00 885.00 253.00 887.00 L 417.00 887.00 L 416.00 701.00 C 393.00 698.00 311.00 700.00 277.00 701.00 C 269.00 701.00 261.00 701.00 253.00 701.00 C 251.00 700.00 243.00 700.00 242.00 701.00 C 234.00 702.00 236.00 699.00 235.00 705.00 C 228.00 710.00 231.00 735.00 231.00 744.00 C 231.00 759.00 229.00 857.00 234.00 865.00 Z" fill="#169053" fill-rule="evenodd" group-id="1" node-id="59" stroke="none" target-height="189" target-width="189" target-x="228" target-y="698"/><path d="M 688.00 594.00 L 697.00 593.00 L 791.00 595.00 L 790.00 656.00 L 613.00 656.00 L 612.00 595.00 L 688.00 594.00 Z M 613.00 591.00 C 610.00 595.00 610.00 587.00 609.00 602.00 L 610.00 659.00 L 767.00 660.00 C 791.00 660.00 794.00 665.00 794.00 640.00 C 794.00 624.00 796.00 606.00 793.00 591.00 L 613.00 591.00 Z" fill="#a9940e" fill-rule="evenodd" group-id="1" node-id="61" stroke="none" target-height="78" target-width="187" target-x="609" target-y="587"/><path d="M 234.00 655.00 C 235.00 660.00 226.00 662.00 253.00 661.00 L 396.00 661.00 C 409.00 661.00 413.00 664.00 417.00 653.00 C 399.00 662.00 351.00 657.00 326.00 657.00 C 304.00 657.00 252.00 661.00 234.00 655.00 L 234.00 594.00 C 295.00 593.00 355.00 594.00 415.00 594.00 C 408.00 590.00 422.00 592.00 404.00 590.00 L 369.00 589.00 C 354.00 589.00 339.00 589.00 323.00 589.00 C 313.00 589.00 241.00 586.00 234.00 594.00 C 232.00 595.00 231.00 590.00 231.00 607.00 C 231.00 617.00 230.00 651.00 234.00 655.00 Z" fill="#776810" fill-rule="evenodd" group-id="1" node-id="63" stroke="none" target-height="78" target-width="196" target-x="226" target-y="586"/><path d="M 461.00 313.00 L 566.00 312.00 L 568.00 461.00 L 460.00 459.00 L 461.00 313.00 Z M 568.00 462.00 C 568.00 462.00 570.00 460.00 571.00 454.00 C 571.00 452.00 571.00 447.00 571.00 445.00 L 568.00 308.00 L 461.00 310.00 C 455.00 326.00 458.00 413.00 457.00 439.00 C 457.00 467.00 452.00 463.00 500.00 464.00 L 561.00 464.00 C 574.00 462.00 565.00 463.00 568.00 462.00 Z" fill="#564809" fill-rule="evenodd" group-id="1" node-id="65" stroke="none" target-height="159" target-width="122" target-x="452" target-y="308"/></g></svg>
                        <p class="mt-2 mb-16 text-gray-400">此分类下还未发布文章哟~</p>
                    </div>
                </div>

                <!-- 分页 -->
                <nav aria-label="Page navigation example" class="mt-10 flex justify-center" v-if="total > 0">
                    <ul class="flex items-center -space-x-px h-10 text-base">
                        <!-- 上一页 -->
                        <li>
                            <a @click="getCategoryArticles(current - 1)"
                                class="flex items-center justify-center px-4 h-10 ml-0 leading-tight text-gray-500 bg-white border border-gray-300 rounded-l-lg hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
                                :class="[current > 1 ? '' : 'cursor-not-allowed']">

                                <span class="sr-only">上一页</span>
                                <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none"
                                    viewBox="0 0 6 10">
                                    <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
                                        stroke-width="2" d="M5 1 1 5l4 4" />
                                </svg>
                            </a>
                        </li>
                        <!-- 页码 -->
                        <li v-for="(pageNo, index) in pages" :key="index">
                            <a @click="getCategoryArticles(pageNo)"
                                class="flex items-center justify-center px-4 h-10 leading-tight border  dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
                                :class="[pageNo == current ? 'text-sky-600  bg-sky-50 border-sky-500 hover:bg-sky-100 hover:text-sky-700' : 'text-gray-500 border-gray-300 bg-white hover:bg-gray-100 hover:text-gray-700']">
                                {{ index + 1 }}
                            </a>
                        </li>
                        <!-- 下一页 -->
                        <li>
                            <a @click="getCategoryArticles(current + 1)"
                                class="flex items-center justify-center px-4 h-10 leading-tight text-gray-500 bg-white border border-gray-300 rounded-r-lg hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
                                :class="[current < pages ? '' : 'cursor-not-allowed']">
                                <span class="sr-only">下一页</span>
                                <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none"
                                    viewBox="0 0 6 10">
                                    <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
                                        stroke-width="2" d="m1 9 4-4-4-4" />
                                </svg>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>

            <!-- 右边侧边栏，占用一列 -->
            <aside class="col-span-4 md:col-span-1">
                <div class="sticky top-[5.5rem]">
                    <!-- 博主信息 -->
                    <UserInfoCard></UserInfoCard>

                    <!-- 分类 -->
                    <CategoryListCard></CategoryListCard>

                    <!-- 标签 -->
                    <TagListCard></TagListCard>
                </div>
            </aside>
        </div>

    </main>

    
    <!-- 返回顶部 -->
    <ScrollToTopButton></ScrollToTopButton>

    <Footer></Footer>
</template>

<script setup>
import Header from '@/layouts/frontend/components/Header.vue'
import Footer from '@/layouts/frontend/components/Footer.vue'
import UserInfoCard from '@/layouts/frontend/components/UserInfoCard.vue'
import TagListCard from '@/layouts/frontend/components/TagListCard.vue'
import CategoryListCard from '@/layouts/frontend/components/CategoryListCard.vue'
import ScrollToTopButton from '@/layouts/frontend/components/ScrollToTopButton.vue'
import { ref, watch,onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCategoryArticlePageList, getCategoryList } from '@/api/frontend/category'

const route = useRoute()
const router = useRouter()

// 文章集合
const articles = ref([])
// 分类名称
const categoryName = ref(route.query.name)
// 分类 ID
const categoryId = ref(route.query.id)

//身份id
const userStr = localStorage.getItem('user')
const userObj = userStr ? JSON.parse(userStr) : null
const userId = userObj?.userInfo?.userID
//获取分类列表
const initCategories = () => {
    // 这里传参必须是 { userId: xxx }，对应后端的 FindCategoryListReqVO
    getCategoryList({ userId: userId }).then((res) => {
        if (res.success) {
            categories.value = res.data
        }
    })
}

// 监听路由
watch(route, (newRoute, oldRoute) => {
    categoryName.value = newRoute.query.name
    categoryId.value = newRoute.query.id
    getCategoryArticles(current.value)
})

// 当前页码
const current = ref(1)
// 每页显示的文章数
const size = ref(10)
// 总文章数
const total = ref(0)
// 总共多少页
const pages = ref(0)

function getCategoryArticles(currentNo) {
    // 上下页是否能点击判断，当要跳转上一页且页码小于 1 时，则不允许跳转；当要跳转下一页且页码大于总页数时，则不允许跳转
    if (currentNo < 1 || (pages.value > 0 && currentNo > pages.value)) return
    // 调用分页接口渲染数据
    getCategoryArticlePageList({ current: currentNo, size: size.value, id: categoryId.value }).then((res) => {
        if (res.success) {
            articles.value = res.data
            current.value = res.current
            size.value = res.size
            total.value = res.total
            pages.value = res.pages
            console.log(articles)
        }
    })
}
getCategoryArticles(current.value)

onMounted(() => {
    initCategories() // 初始化顶部分类导航
    getCategoryArticles(current.value) // 加载文章列表
})

// 跳转文章详情页
const goArticleDetailPage = (articleId) => {
    router.push('/article/' + articleId)
}

// 所有分类
const categories = ref([])
getCategoryList({}).then((res) => {
    if (res.success) {
        categories.value = res.data
    }
})

// 跳转分类文章列表页
const goCategoryArticleListPage = (id, name) => {
    // 跳转时通过 query 携带参数（分类 ID、分类名称）
    router.push({ path: '/category/article/list', query: { id, name } })
}
</script>
