const CACHE_NAME = 'cache-v2';
const STATIC_CACHE_URLS = [
    './',
    './scripts/app.js',
    './scripts/Calendar.js',
    './scripts/Day.js',
    './scripts/Feriado.js',
    './scripts/GUI.js',
    './manifest.webmanifest',
    './images/icon_512.png',
    './images/icon.svg',
    './index.css',
    './index.html'
];

this.addEventListener('install', event => {
    event.waitUntil(caches.open(CACHE_NAME).then(cache => cache.addAll(STATIC_CACHE_URLS)));
});

this.addEventListener('activate', event => {
    console.log('Service worker activate event!');
//    var cacheWhitelist = ['v2'];
//    event.waitUntil(
//            caches.keys().then(keyList => Promise.all(keyList.map(key => {
//            if (cacheWhitelist.indexOf(key) === -1) {
//                return caches.delete(key);
//            }
//        }))
//    ));
});

this.addEventListener('fetch', event => {
    event.respondWith(caches.match(event.request).then(response => response || fetch(event.request)));
//    event.respondWith(caches.match(event.request).then(resp => {
//        return resp || fetch(event.request).then(response => {
//            return caches.open('v1').then(cache => {
//                cache.put(event.request, response.clone());
//                return response;
//            });
//        }).catch(() => caches.match('/sw-test/gallery/myLittleVader.jpg'));
//    })
//            );
});