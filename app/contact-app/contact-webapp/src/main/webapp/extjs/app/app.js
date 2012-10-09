Ext.application({
    name: 'Pandora',
    
    autoCreateViewport: true,
    
    models: ['Station', 'Song'],    
    stores: ['PersonStore', 'Stations', 'RecentSongs', 'SearchResults'],
    controllers: ['Station', 'Song']
});