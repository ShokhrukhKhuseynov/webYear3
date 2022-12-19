
const app = new Vue({
    el: "#app",
    data: {
        laptops: [],
        data: [],
        comparisons: [],
        brand: undefined,
        model: undefined,
        page: undefined,
        laptopId: undefined
    },
    methods: {

        searchProducts: function () {
            const localApp = this;

            localApp.getSearchQuery();
            localApp.getPage();

            axios.get("/search?brand=" + localApp.brand + "&model=" + localApp.model + "&num_items=" + 9 + "&page=" + localApp.page)
                .then(function (response) {
                    localApp.data = response.data.data;
                    localApp.laptops = localApp.data;
                    localApp.changeStyle();
                    localApp.handleButtons();
                })
                .catch(function (err) {
                    console.log(err);
                });
        },
        searchComparisons: function (laptopID) {
            const localApp = this;

            axios.get("/compare/" + laptopID).then(function (response) {
                localApp.comparisons = response.data;
                localApp.comparisons.sort((a, b) => a.price - b.price);
                console.log(response.data);
            }).catch(function (err) {
                console.log(err);
            });
        },

        getProductDetail: function () {
            const target = event.target;

            if (target.className === "product-wrapper") {
                this.laptopId = +target.id
            } else if (target.className === "product-image" || target.className === "product-titler" || target.className === "product-sub-title") {
                this.laptopId = +target.parentNode.id;
            } else if (target.className === "product-price") {
                this.laptopId = +target.parentNode.parentNode.id;
            }
            if (this.laptopId !== undefined) {
                document.getElementById("products-container").style.display = "none";
                document.getElementById("filter-container").style.display = "none";
                document.getElementById("product-detail-container").style.display = "block";
                this.searchComparisons(this.laptopId);
            }


        },
        getPage: function () {
            const pageLabel = document.getElementsByClassName('page-label')[0];
            const currentPage = +pageLabel.id.charAt(pageLabel.id.length - 1);
            this.page = currentPage;
            pageLabel.innerText = this.page + 1;
        },

        nextPage: function () {
            const pageLabel = document.getElementsByClassName('page-label')[0];
            const currentPage = +pageLabel.id.charAt(pageLabel.id.length - 1);
            pageLabel.id = "page-" + (currentPage + 1);
            this.searchProducts();
        },

        prevPage: function () {
            const pageLabel = document.getElementsByClassName('page-label')[0];
            const currentPage = +pageLabel.id.charAt(pageLabel.id.length - 1);
            pageLabel.id = "page-" + (currentPage - 1);
            this.searchProducts();
        },

        handleButtons: function () {
            const prevButton = document.getElementsByClassName('pagenation-button')[0];
            const nextButton = document.getElementsByClassName('pagenation-button')[1];
            if (this.laptops.length < 9 && this.page === 0) {
                nextButton.style.pointerEvents = "none";
                nextButton.style.color = "#6B728E";
                nextButton.style.backgroundColor = "white";
                nextButton.style.border = "1px solid #6B728E";
                prevButton.style.pointerEvents = "none";
                prevButton.style.color = "#6B728E";
                prevButton.style.backgroundColor = "white";
                prevButton.style.border = "1px solid #6B728E";

            } else if (this.laptops.length < 9) {
                nextButton.style.pointerEvents = "none";
                nextButton.style.color = "#6B728E";
                nextButton.style.backgroundColor = "white";
                nextButton.style.border = "1px solid #6B728E";
                if (prevButton.style.pointerEvents === "none") {
                    prevButton.style.pointerEvents = "all"
                    prevButton.style.color = "white";
                    prevButton.style.backgroundColor = "#0D4C92";
                }

            } else if (this.page === 0) {
                prevButton.style.pointerEvents = "none";
                prevButton.style.color = "#6B728E";
                prevButton.style.backgroundColor = "white";
                prevButton.style.border = "1px solid #6B728E";
                if (nextButton.style.pointerEvents === "none") {
                    nextButton.style.pointerEvents = "all"
                    nextButton.style.color = "white";
                    nextButton.style.backgroundColor = "#0D4C92";
                }
            } else {
                if (prevButton.style.pointerEvents === "none") {
                    prevButton.style.pointerEvents = "all"
                    prevButton.style.color = "white";
                    prevButton.style.backgroundColor = "#0D4C92";
                }
                if (nextButton.style.pointerEvents === "none") {
                    nextButton.style.pointerEvents = "all"
                    nextButton.style.color = "white";
                    nextButton.style.backgroundColor = "#0D4C92";
                }
            }
        },
        getSearchQuery: function () {
            const localApp = this;
            const searchField = document.getElementById('search-text-field');

            localApp.brand = undefined;
            localApp.model = undefined;

            const brands = ["apple", "microsoft"];
            const models = ["macbook", "air", "pro"];
            const searchQuery = searchField.value;
            const words = searchQuery.split(" ");

            if (searchQuery) {
                brands.forEach(brand => {
                    words.forEach(word => {
                        if (brand === word.toLowerCase()) localApp.brand = brand;
                    });
                });
                models.forEach(model => {
                    words.forEach(word => {
                        if (model === word.toLowerCase()) localApp.model = model;
                    });
                });
                if (localApp.model === "macbook") {
                    localApp.model = undefined;
                    localApp.brand = "apple";
                } else if (localApp.model === "air" || localApp.model === "pro") {
                    localApp.brand = "apple"
                    localApp.model = "macbook ".concat(localApp.model);
                }
                console.log(localApp.brand);
                console.log(localApp.model);

            }
        },
        sortProducts: function () {
            let localApp = this;
            const radioButtons = document.querySelectorAll('input[name="option"]');
            const checkBox = document.querySelectorAll('input[name="checkbox"]')[0];
            radioButtons.forEach(radio => {
                if (radio.checked) {
                    if (radio.value !== "price")
                        localApp.laptops = localApp.laptops.sort((a, b) => +a[radio.value].slice(0, a[radio.value].length - 2) - +b[radio.value].slice(0, b[radio.value].length - 2));
                    else
                        localApp.laptops = localApp.laptops.sort((a, b) => a[radio.value] - b[radio.value]);

                    if (checkBox.checked)
                        localApp.laptops = localApp.laptops.reverse();
                    return;
                }
            })

        },



        changeStyle: function () {

            document.getElementById("search-container").style.backgroundColor = "#263159";
            document.getElementsByClassName("splide")[0].style.display = "none";
            document.getElementById("products-container").style.display = "grid";
            document.getElementById("filter-container").style.display = "flex";
            document.getElementById("product-detail-container").style.display = "none";

        },

        goBack:function(){
            this.changeStyle();
            this.handleButtons();
        },
        goHome: function(){

            document.getElementById("search-container").style.backgroundColor = "hsla(0,0%,100%,.15)";
            document.getElementsByClassName("splide")[0].style.display = "block";
            document.getElementById("products-container").style.display = "none";
            document.getElementById("filter-container").style.display = "none";
            document.getElementById("product-detail-container").style.display = "none";
            document.getElementsByClassName("splide__pagination")[0].style.display = "flex"
        }
    }
});


if (document.getElementsByClassName('splide')[0]) {
    const splide = new Splide('.splide', {
        type: 'loop',
        perPage: 1,
        autoplay: true,
        pauseOnHover: false,
        pauseOnFocus: false,
    });
    splide.mount();
}
