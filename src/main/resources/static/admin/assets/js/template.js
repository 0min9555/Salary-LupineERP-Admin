(function ($) {
    'use strict';
    $(function () {
        var body = $('body');
        var mainWrapper = $('.main-wrapper');
        var footer = $('footer');
        var sidebar = $('.sidebar');
        var navbar = $('.navbar').not('.top-navbar');

        // Enable feather-icons with SVG markup
        feather.replace();

        // initialize clipboard plugin
        if ($('.btn-clipboard').length) {
            // Enabling tooltip to all clipboard buttons
            $('.btn-clipboard')
                .attr('data-bs-toggle', 'tooltip')
                .attr('title', 'Copy to clipboard');

            var clipboard = new ClipboardJS('.btn-clipboard');

            clipboard.on('success', function (e) {
                console.log(e);
                e.trigger.innerHTML = 'copied';
                setTimeout(function () {
                    e.trigger.innerHTML = 'copy';
                    e.clearSelection();
                }, 700)
            });
        }

        // initializing bootstrap tooltip
        var tooltipTriggerList = []
            .slice
            .call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
        var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl)
        })

        // initializing bootstrap popover
        var popoverTriggerList = []
            .slice
            .call(document.querySelectorAll('[data-bs-toggle="popover"]'))
        var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
            return new bootstrap.Popover(popoverTriggerEl)
        })

        // Applying perfect-scrollbar
        if ($('.sidebar .sidebar-body').length) {
            const sidebarBodyScroll = new PerfectScrollbar('.sidebar-body');
        }
        // commented beacuse of hang (scroll from  dropdown.html with small height) if
        // ($('.content-nav-wrapper').length) {   const contentNavWrapper = new
        // PerfectScrollbar('.content-nav-wrapper'); } Close other submenu in sidebar on
        // opening any
        sidebar.on('show.bs.collapse', '.collapse', function () {
            sidebar
                .find('.collapse.show')
                .collapse('hide');
        });

        // Sidebar toggle to sidebar-folded
        $('.sidebar-toggler').on('click', function (e) {
            e.preventDefault();
            $('.sidebar-header .sidebar-toggler').toggleClass('active not-active');
            if (window.matchMedia('(min-width: 992px)').matches) {
                e.preventDefault();
                body.toggleClass('sidebar-folded');
            } else if (window.matchMedia('(max-width: 991px)').matches) {
                e.preventDefault();
                body.toggleClass('sidebar-open');
            }
        });

        // commmented because of apex chart width issue in desktop (in lg not in xl)
        // sidebar-folded on large devices function iconSidebar(e) {   if (e.matches) {
        // body.addClass('sidebar-folded');   } else {
        // body.removeClass('sidebar-folded');   } } var desktopMedium =
        // window.matchMedia('(min-width:992px) and (max-width: 1199px)');
        // desktopMedium.addListener(iconSidebar); iconSidebar(desktopMedium); Settings
        // sidebar toggle
        $('.settings-sidebar-toggler').on('click', function (e) {
            $('body').toggleClass('settings-open');
        });

        // Sidebar theme settings
        $("input:radio[name=sidebarThemeSettings]").click(function () {
            $('body').removeClass('sidebar-light sidebar-dark');
            $('body').addClass($(this).val());
        })

        //Add active class to nav-link based on url dynamically
        function addActiveClass(element) {
            if (current === "") {
                //for root url
                if (element.attr('href').indexOf("index.html") !== -1) {
                    element
                        .parents('.nav-item')
                        .last()
                        .addClass('active');
                    if (element.parents('.sub-menu').length) {
                        element
                            .closest('.collapse')
                            .addClass('show');
                        element.addClass('active');
                    }
                }
            } else {
                //for other url
                if (element.attr('href').indexOf(current) !== -1) {
                    element
                        .parents('.nav-item')
                        .last()
                        .addClass('active');
                    if (element.parents('.sub-menu').length) {
                        element
                            .closest('.collapse')
                            .addClass('show');
                        element.addClass('active');
                    }
                    if (element.parents('.submenu-item').length) {
                        element.addClass('active');
                    }
                }
            }
        }

        // var current = location
        //     .pathname
        //     .split("/")
        //     .slice(-2)[0]
        //     .replace(/^\/|\/$/g, '');
        // $('.nav li a', sidebar).each(function () {
        //     var $this = $(this);
        //     addActiveClass($this);
        // });

        // $('.horizontal-menu .nav li a').each(function () {
        //     var $this = $(this);
        //     addActiveClass($this);
        // })

        // $('.page-wrapper .page-content .content-nav-wrapper .content-nav li a').each(
        //     function () {
        //         var $this = $(this);
        //         addActiveClass($this);
        //     }
        // )

        //  open sidebar-folded when hover
        $(".sidebar .sidebar-body").hover(function () {
            if (body.hasClass('sidebar-folded')) {
                body.addClass("open-sidebar-folded");
            }
        }, function () {
            if (body.hasClass('sidebar-folded')) {
                body.removeClass("open-sidebar-folded");
            }
        });

        // close sidebar when click outside on mobile/table
        $(document).on('click touchstart', function (e) {
            e.stopPropagation();

            // closing of sidebar menu when clicking outside of it
            if (!$(e.target).closest('.sidebar-toggler').length) {
                var sidebar = $(e.target)
                    .closest('.sidebar')
                    .length;
                var sidebarBody = $(e.target)
                    .closest('.sidebar-body')
                    .length;
                if (!sidebar && !sidebarBody) {
                    if ($('body').hasClass('sidebar-open')) {
                        $('body').removeClass('sidebar-open');
                    }
                }
            }
        });

        //Horizontal menu in mobile
        $('[data-toggle="horizontal-menu-toggle"]').on("click", function () {
            $(".horizontal-menu .bottom-navbar").toggleClass("header-toggled");
        });
        // Horizontal menu navigation in mobile menu on click
        var navItemClicked = $('.horizontal-menu .page-navigation >.nav-item');
        navItemClicked.on("click", function (event) {
            if (window.matchMedia('(max-width: 991px)').matches) {
                if (!($(this).hasClass('show-submenu'))) {
                    navItemClicked.removeClass('show-submenu');
                }
                $(this).toggleClass('show-submenu');
            }
        })

        $(window).scroll(function () {
            // if (window.matchMedia('(min-width: 992px)').matches) {     var header =
            // $('.horizontal-menu');     if ($(window).scrollTop() >= 60) {
            // $(header).addClass('fixed-on-scroll');     } else {
            // $(header).removeClass('fixed-on-scroll');     } }
        });

        // Prevent body scrolling while sidebar scroll
        $('.sidebar .sidebar-body').hover(function () {
            $('body').addClass('overflow-hidden');
        }, function () {
            $('body').removeClass('overflow-hidden');
        });

        // tab menu
        $(document).on('click', '.tab-btn-group button' , function () {
            $(this).siblings('button').removeClass('active');
            $(this).addClass('active');
        })

        $(document).on('click', '.tab-tit .btn' , function () {
            $(this).siblings('input[type="text"]').attr('disabled',false).focus();
        })

        // checkbox
        $(document).on('click', '#chkAll' , function () {
            let isChecked = $(this).is(':checked');
            $(this)
                .parents('table')
                .find('tbody')
                .find(':checkbox')
                .prop('checked', isChecked);
        })

        // 미리보기 모달창 오픈
        $('.btn-preview').on('click', function () {
            let imgSrc = $(this).data('src');
            $('#previewModal img').attr('src', imgSrc);
            $('#previewModal').fadeIn();
            $('body').addClass('preview-open');
        })

        $('.preview-cell .preview-btn').on('click', function () {
            let imgSrc = $(this)
                .find('img')
                .attr('src');
            $('#previewModal img').attr('src', imgSrc);
            $('#previewModal').fadeIn();
            $('body').addClass('preview-open');
        });

        // 미리보기 모달창 클로즈
        $('#previewModal .btn-close,  #previewModal .modal-preview-back').on(
            'click',
            function () {
                $('#previewModal').fadeOut();
                $('body').removeClass('preview-open');
            }
        )

        $('.file-control').on('change', function () {
            var file = $(this)
                    .get(0)
                    .files,
                $target = $(this)
                    .parent()
                    .parent()
                    .find('.btn-preview');
            var reader = new FileReader();
            reader.readAsDataURL(file[0]);
            reader.addEventListener('load', function (e) {
                var imgPath = e.target.result;
                $target.prop('disabled', false);
                $target.data('src', imgPath);
            });
        });

    });

})(jQuery);